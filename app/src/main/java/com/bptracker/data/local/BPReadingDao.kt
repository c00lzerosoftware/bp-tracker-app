package com.bptracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface BPReadingDao {

    @Query("SELECT * FROM bp_readings ORDER BY timestamp DESC")
    fun getAllReadingsFlow(): Flow<List<BPReading>>

    @Query("SELECT * FROM bp_readings ORDER BY timestamp DESC")
    suspend fun getAllReadings(): List<BPReading>

    @Query("SELECT * FROM bp_readings WHERE id = :id")
    suspend fun getReadingById(id: Long): BPReading?

    @Query("SELECT * FROM bp_readings WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    suspend fun getReadingsByDateRange(startTime: Instant, endTime: Instant): List<BPReading>

    @Query("SELECT * FROM bp_readings WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getReadingsByDateRangeFlow(startTime: Instant, endTime: Instant): Flow<List<BPReading>>

    @Query("SELECT * FROM bp_readings ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentReadings(limit: Int): List<BPReading>

    @Query("SELECT * FROM bp_readings ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentReadingsFlow(limit: Int): Flow<List<BPReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: BPReading): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadings(readings: List<BPReading>)

    @Update
    suspend fun updateReading(reading: BPReading)

    @Delete
    suspend fun deleteReading(reading: BPReading)

    @Query("DELETE FROM bp_readings WHERE id = :id")
    suspend fun deleteReadingById(id: Long)

    @Query("DELETE FROM bp_readings")
    suspend fun deleteAllReadings()

    @Query("SELECT COUNT(*) FROM bp_readings")
    suspend fun getReadingsCount(): Int

    @Query("SELECT AVG(systolic) FROM bp_readings WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getAverageSystolic(startTime: Instant, endTime: Instant): Double?

    @Query("SELECT AVG(diastolic) FROM bp_readings WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getAverageDiastolic(startTime: Instant, endTime: Instant): Double?

    @Query("SELECT AVG(pulse) FROM bp_readings WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getAveragePulse(startTime: Instant, endTime: Instant): Double?
}
