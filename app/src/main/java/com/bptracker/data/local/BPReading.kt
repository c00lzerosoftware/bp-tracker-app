package com.bptracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "bp_readings")
data class BPReading(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val timestamp: Instant,
    val notes: String? = null,
    val imageUri: String? = null,
    val source: ReadingSource = ReadingSource.MANUAL,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

enum class ReadingSource {
    MANUAL,
    SCANNED
}
