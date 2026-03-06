package com.bptracker.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilli()
    }

    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromDaysOfWeek(value: Set<DayOfWeek>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toDaysOfWeek(value: String?): Set<DayOfWeek>? {
        return value?.let {
            val type = object : TypeToken<Set<DayOfWeek>>() {}.type
            gson.fromJson(it, type)
        }
    }

    @TypeConverter
    fun fromReadingSource(value: ReadingSource?): String? {
        return value?.name
    }

    @TypeConverter
    fun toReadingSource(value: String?): ReadingSource? {
        return value?.let { ReadingSource.valueOf(it) }
    }
}
