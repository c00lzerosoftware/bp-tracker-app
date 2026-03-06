package com.bptracker.data.remote.models

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
)

data class Content(
    val parts: List<Part>,
    val role: String = "user"
)

sealed class Part {
    data class TextPart(val text: String) : Part()
    data class InlineDataPart(
        @SerializedName("inlineData")
        val inlineData: InlineData
    ) : Part()
}

data class InlineData(
    val mimeType: String,
    val data: String // Base64 encoded
)

data class GenerationConfig(
    val temperature: Double? = null,
    val topK: Int? = null,
    val topP: Double? = null,
    val maxOutputTokens: Int? = null
)

data class GeminiResponse(
    val candidates: List<Candidate>?,
    val promptFeedback: PromptFeedback?
)

data class Candidate(
    val content: Content,
    val finishReason: String?,
    val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    val category: String,
    val probability: String
)

data class PromptFeedback(
    val safetyRatings: List<SafetyRating>?
)
