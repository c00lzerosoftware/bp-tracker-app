package com.bptracker.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

enum class DateRangeType {
    WEEK,
    MONTH,
    THREE_MONTHS,
    SIX_MONTHS,
    YEAR,
    ALL_TIME,
    CUSTOM
}

data class DateRange(
    val startTime: Instant,
    val endTime: Instant,
    val type: DateRangeType = DateRangeType.CUSTOM
) {
    companion object {
        fun fromType(type: DateRangeType, zoneId: ZoneId = ZoneId.systemDefault()): DateRange {
            val now = Instant.now()
            val today = LocalDate.now(zoneId)

            val startTime = when (type) {
                DateRangeType.WEEK -> {
                    today.minusWeeks(1).atStartOfDay(zoneId).toInstant()
                }
                DateRangeType.MONTH -> {
                    today.minusMonths(1).atStartOfDay(zoneId).toInstant()
                }
                DateRangeType.THREE_MONTHS -> {
                    today.minusMonths(3).atStartOfDay(zoneId).toInstant()
                }
                DateRangeType.SIX_MONTHS -> {
                    today.minusMonths(6).atStartOfDay(zoneId).toInstant()
                }
                DateRangeType.YEAR -> {
                    today.minusYears(1).atStartOfDay(zoneId).toInstant()
                }
                DateRangeType.ALL_TIME -> {
                    Instant.EPOCH
                }
                DateRangeType.CUSTOM -> {
                    today.minusMonths(1).atStartOfDay(zoneId).toInstant()
                }
            }

            return DateRange(
                startTime = startTime,
                endTime = now,
                type = type
            )
        }

        fun custom(startTime: Instant, endTime: Instant): DateRange {
            return DateRange(
                startTime = startTime,
                endTime = endTime,
                type = DateRangeType.CUSTOM
            )
        }
    }

    fun getDurationInDays(): Long {
        return ChronoUnit.DAYS.between(startTime, endTime)
    }
}
