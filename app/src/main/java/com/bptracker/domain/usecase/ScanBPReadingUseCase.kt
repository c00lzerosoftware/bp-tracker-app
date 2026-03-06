package com.bptracker.domain.usecase

import com.bptracker.data.local.ReadingSource
import com.bptracker.data.remote.GeminiClient
import com.bptracker.data.remote.models.BPReadingResult
import com.bptracker.data.repository.BPReadingRepository
import java.time.Instant
import javax.inject.Inject

class ScanBPReadingUseCase @Inject constructor(
    private val geminiClient: GeminiClient,
    private val repository: BPReadingRepository
) {

    suspend fun extractReading(imageBytes: ByteArray): Result<BPReadingResult> {
        return geminiClient.extractBPReading(imageBytes)
    }

    suspend fun saveScannedReading(
        readingResult: BPReadingResult,
        imageUri: String? = null,
        notes: String? = null
    ): Result<Long> {
        return try {
            val reading = com.bptracker.data.local.BPReading(
                systolic = readingResult.systolic,
                diastolic = readingResult.diastolic,
                pulse = readingResult.pulse,
                timestamp = Instant.now(),
                notes = notes,
                imageUri = imageUri,
                source = ReadingSource.SCANNED
            )

            val id = repository.insertReading(reading)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
