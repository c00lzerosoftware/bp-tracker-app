package com.bptracker.data.remote

import android.util.Base64
import com.bptracker.BuildConfig
import com.bptracker.data.local.BPReading
import com.bptracker.data.remote.models.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor(
    private val apiService: GeminiApiService,
    private val gson: Gson
) {

    /**
     * Extract BP reading from an image using Gemini Vision
     */
    suspend fun extractBPReading(imageBytes: ByteArray): Result<BPReadingResult> =
        withContext(Dispatchers.IO) {
            try {
                val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

                val prompt = """
                    Analyze this image of a blood pressure monitor display.
                    Extract the following values:
                    - Systolic pressure (top number)
                    - Diastolic pressure (bottom number)
                    - Pulse/heart rate

                    Return ONLY a JSON object with this exact format:
                    {"systolic": <number>, "diastolic": <number>, "pulse": <number>}

                    If you cannot clearly read any value, return null for that field.
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(
                                Part.TextPart(prompt),
                                Part.InlineDataPart(
                                    InlineData(
                                        mimeType = "image/jpeg",
                                        data = base64Image
                                    )
                                )
                            )
                        )
                    ),
                    generationConfig = GenerationConfig(
                        temperature = 0.1,
                        maxOutputTokens = 256
                    )
                )

                val response = apiService.generateContent(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = request
                )

                val textResponse = response.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.filterIsInstance<Part.TextPart>()
                    ?.firstOrNull()
                    ?.text
                    ?: return@withContext Result.failure(Exception("No response from Gemini"))

                // Extract JSON from response (handling markdown code blocks)
                val jsonText = extractJson(textResponse)

                val result = gson.fromJson(jsonText, BPReadingResult::class.java)
                    ?: return@withContext Result.failure(Exception("Failed to parse response"))

                Result.success(result)
            } catch (e: JsonSyntaxException) {
                Result.failure(Exception("Failed to parse Gemini response: ${e.message}", e))
            } catch (e: Exception) {
                Result.failure(Exception("Failed to extract BP reading: ${e.message}", e))
            }
        }

    /**
     * Generate health insights based on BP readings
     */
    suspend fun generateInsights(readings: List<BPReading>): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val readingsData = readings.map {
                    mapOf(
                        "systolic" to it.systolic,
                        "diastolic" to it.diastolic,
                        "pulse" to it.pulse,
                        "timestamp" to it.timestamp.toString()
                    )
                }

                val prompt = """
                    You are a health advisor assistant. Analyze the following blood pressure readings:

                    ${gson.toJson(readingsData)}

                    Provide a brief health insight summary (3-4 sentences) covering:
                    1. Overall trend (improving, stable, or concerning)
                    2. Any notable patterns or outliers
                    3. General health recommendations

                    Be supportive and informative. Do NOT provide medical diagnosis.
                    Always recommend consulting a healthcare provider for medical advice.
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(Part.TextPart(prompt))
                        )
                    ),
                    generationConfig = GenerationConfig(
                        temperature = 0.7,
                        maxOutputTokens = 512
                    )
                )

                val response = apiService.generateContent(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = request
                )

                val insight = response.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.filterIsInstance<Part.TextPart>()
                    ?.firstOrNull()
                    ?.text
                    ?: return@withContext Result.failure(Exception("No insights generated"))

                Result.success(insight)
            } catch (e: Exception) {
                Result.failure(Exception("Failed to generate insights: ${e.message}", e))
            }
        }

    /**
     * Extract JSON from text that might contain markdown code blocks
     */
    private fun extractJson(text: String): String {
        val jsonBlockRegex = """```(?:json)?\s*(\{.*?\})\s*```""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val match = jsonBlockRegex.find(text)
        return match?.groupValues?.get(1)?.trim() ?: text.trim()
    }
}
