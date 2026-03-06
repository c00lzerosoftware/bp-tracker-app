package com.bptracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BPReading::class, Reminder::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BPDatabase : RoomDatabase() {
    abstract fun bpReadingDao(): BPReadingDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        const val DATABASE_NAME = "bp_tracker.db"
    }
}
