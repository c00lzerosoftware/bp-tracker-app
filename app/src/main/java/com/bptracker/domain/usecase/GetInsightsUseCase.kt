package com.bptracker.domain.usecase

import com.bptracker.data.repository.BPReadingRepository
import com.bptracker.data.repository.InsightsRepository
import com.bptracker.domain.model.DateRange
import com.bptracker.domain.model.DateRangeType
import javax.inject.Inject

class GetInsightsUseCase @Inject constructor(
    private val readingRepository: BPReadingRepository,
    private val insightsRepository: InsightsRepository
) {

    suspend operator fun invoke(dateRange: DateRange? = null): Result<String> {
        return try {
            val range = dateRange ?: DateRange.fromType(DateRangeType.MONTH)

            val readings = readingRepository.getReadingsByDateRange(
                startTime = range.startTime,
                endTime = range.endTime
            )

            if (readings.isEmpty()) {
                return Result.failure(
                    Exception("No readings available for the selected period")
                )
            }

            insightsRepository.generateInsights(readings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
