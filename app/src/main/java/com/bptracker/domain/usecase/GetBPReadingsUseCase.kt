package com.bptracker.domain.usecase

import com.bptracker.data.local.BPReading
import com.bptracker.data.repository.BPReadingRepository
import com.bptracker.domain.model.DateRange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBPReadingsUseCase @Inject constructor(
    private val repository: BPReadingRepository
) {

    fun getAllReadings(): Flow<List<BPReading>> {
        return repository.getAllReadings()
    }

    fun getReadingsByDateRange(dateRange: DateRange): Flow<List<BPReading>> {
        return repository.getReadingsByDateRangeFlow(
            startTime = dateRange.startTime,
            endTime = dateRange.endTime
        )
    }

    fun getRecentReadings(limit: Int = 30): Flow<List<BPReading>> {
        return repository.getRecentReadingsFlow(limit)
    }

    suspend fun getReadingById(id: Long): BPReading? {
        return repository.getReadingById(id)
    }
}
