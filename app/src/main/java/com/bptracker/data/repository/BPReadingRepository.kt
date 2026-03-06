package com.bptracker.data.repository

import com.bptracker.data.local.BPReading
import com.bptracker.data.local.BPReadingDao
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BPReadingRepository @Inject constructor(
    private val bpReadingDao: BPReadingDao
) {

    fun getAllReadings(): Flow<List<BPReading>> =
        bpReadingDao.getAllReadingsFlow()

    suspend fun getReadingById(id: Long): BPReading? =
        bpReadingDao.getReadingById(id)

    suspend fun getReadingsByDateRange(
        startTime: Instant,
        endTime: Instant
    ): List<BPReading> =
        bpReadingDao.getReadingsByDateRange(startTime, endTime)

    fun getReadingsByDateRangeFlow(
        startTime: Instant,
        endTime: Instant
    ): Flow<List<BPReading>> =
        bpReadingDao.getReadingsByDateRangeFlow(startTime, endTime)

    suspend fun getRecentReadings(limit: Int = 30): List<BPReading> =
        bpReadingDao.getRecentReadings(limit)

    fun getRecentReadingsFlow(limit: Int = 30): Flow<List<BPReading>> =
        bpReadingDao.getRecentReadingsFlow(limit)

    suspend fun insertReading(reading: BPReading): Long =
        bpReadingDao.insertReading(reading)

    suspend fun updateReading(reading: BPReading) =
        bpReadingDao.updateReading(reading)

    suspend fun deleteReading(reading: BPReading) =
        bpReadingDao.deleteReading(reading)

    suspend fun deleteReadingById(id: Long) =
        bpReadingDao.deleteReadingById(id)

    suspend fun getReadingsCount(): Int =
        bpReadingDao.getReadingsCount()

    suspend fun getAverageReadings(
        startTime: Instant,
        endTime: Instant
    ): AverageReadings {
        val avgSystolic = bpReadingDao.getAverageSystolic(startTime, endTime) ?: 0.0
        val avgDiastolic = bpReadingDao.getAverageDiastolic(startTime, endTime) ?: 0.0
        val avgPulse = bpReadingDao.getAveragePulse(startTime, endTime) ?: 0.0

        return AverageReadings(
            systolic = avgSystolic,
            diastolic = avgDiastolic,
            pulse = avgPulse
        )
    }
}

data class AverageReadings(
    val systolic: Double,
    val diastolic: Double,
    val pulse: Double
)
