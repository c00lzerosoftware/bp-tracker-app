package com.bptracker.data.repository

import com.bptracker.data.local.BPReading
import com.bptracker.data.remote.GeminiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsightsRepository @Inject constructor(
    private val geminiClient: GeminiClient
) {

    suspend fun generateInsights(readings: List<BPReading>): Result<String> {
        if (readings.isEmpty()) {
            return Result.failure(Exception("No readings available for insights"))
        }

        return geminiClient.generateInsights(readings)
    }
}
