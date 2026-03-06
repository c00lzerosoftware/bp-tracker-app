package com.bptracker.domain.usecase

import com.bptracker.data.local.BPReading
import com.bptracker.data.local.ReadingSource
import com.bptracker.data.repository.BPReadingRepository
import java.time.Instant
import javax.inject.Inject

class AddBPReadingUseCase @Inject constructor(
    private val repository: BPReadingRepository
) {

    suspend operator fun invoke(
        systolic: Int,
        diastolic: Int,
        pulse: Int,
        timestamp: Instant = Instant.now(),
        notes: String? = null,
        imageUri: String? = null,
        source: ReadingSource = ReadingSource.MANUAL
    ): Result<Long> {
        return try {
            // Validate readings
            if (!isValidReading(systolic, diastolic, pulse)) {
                return Result.failure(
                    IllegalArgumentException("Invalid reading values")
                )
            }

            val reading = BPReading(
                systolic = systolic,
                diastolic = diastolic,
                pulse = pulse,
                timestamp = timestamp,
                notes = notes,
                imageUri = imageUri,
                source = source
            )

            val id = repository.insertReading(reading)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isValidReading(systolic: Int, diastolic: Int, pulse: Int): Boolean {
        return systolic in 70..250 &&
                diastolic in 40..150 &&
                pulse in 40..200 &&
                systolic > diastolic
    }
}
