package com.bptracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val time: LocalTime,
    val daysOfWeek: Set<DayOfWeek>,
    val isEnabled: Boolean = true,
    val label: String? = null
)
