package com.bptracker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateTimeExtensions {

    fun Instant.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
        return LocalDateTime.ofInstant(this, zoneId)
    }

    fun Instant.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
        return toLocalDateTime(zoneId).toLocalDate()
    }

    fun Instant.formatDate(zoneId: ZoneId = ZoneId.systemDefault()): String {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        return toLocalDateTime(zoneId).format(formatter)
    }

    fun Instant.formatTime(zoneId: ZoneId = ZoneId.systemDefault()): String {
        val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        return toLocalDateTime(zoneId).format(formatter)
    }

    fun Instant.formatDateTime(zoneId: ZoneId = ZoneId.systemDefault()): String {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        return toLocalDateTime(zoneId).format(formatter)
    }

    fun Instant.isToday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
        val today = LocalDate.now(zoneId)
        val dateToCheck = toLocalDate(zoneId)
        return dateToCheck == today
    }

    fun Instant.isYesterday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
        val yesterday = LocalDate.now(zoneId).minusDays(1)
        val dateToCheck = toLocalDate(zoneId)
        return dateToCheck == yesterday
    }

    fun Instant.getRelativeDateString(zoneId: ZoneId = ZoneId.systemDefault()): String {
        return when {
            isToday(zoneId) -> "Today"
            isYesterday(zoneId) -> "Yesterday"
            else -> formatDate(zoneId)
        }
    }
}
