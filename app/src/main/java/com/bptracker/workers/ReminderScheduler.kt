package com.bptracker.workers

import android.content.Context
import androidx.work.*
import com.bptracker.data.local.Reminder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(reminder: Reminder) {
        if (!reminder.isEnabled) {
            cancelReminder(reminder.id)
            return
        }

        // Schedule for each selected day of the week
        reminder.daysOfWeek.forEach { dayOfWeek ->
            scheduleReminderForDay(reminder, dayOfWeek)
        }
    }

    private fun scheduleReminderForDay(reminder: Reminder, dayOfWeek: DayOfWeek) {
        val workName = getWorkName(reminder.id, dayOfWeek)

        val initialDelay = calculateInitialDelay(reminder, dayOfWeek)

        val inputData = workDataOf(
            ReminderWorker.KEY_REMINDER_ID to reminder.id,
            ReminderWorker.KEY_LABEL to reminder.label
        )

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()

        val reminderWork = PeriodicWorkRequestBuilder<ReminderWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .setConstraints(constraints)
            .addTag(getReminderTag(reminder.id))
            .build()

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.UPDATE,
            reminderWork
        )
    }

    private fun calculateInitialDelay(reminder: Reminder, targetDayOfWeek: DayOfWeek): Duration {
        val now = LocalDateTime.now()
        val zoneId = ZoneId.systemDefault()

        var targetDateTime = LocalDate.now()
            .with(targetDayOfWeek)
            .atTime(reminder.time)

        // If the target time is in the past today, schedule for next week
        if (targetDateTime.isBefore(now)) {
            targetDateTime = targetDateTime.plusWeeks(1)
        }

        val nowInstant = now.atZone(zoneId).toInstant()
        val targetInstant = targetDateTime.atZone(zoneId).toInstant()

        return Duration.between(nowInstant, targetInstant)
    }

    fun cancelReminder(reminderId: Long) {
        workManager.cancelAllWorkByTag(getReminderTag(reminderId))
    }

    fun cancelAllReminders() {
        workManager.cancelAllWorkByTag(REMINDER_TAG)
    }

    private fun getWorkName(reminderId: Long, dayOfWeek: DayOfWeek): String {
        return "reminder_${reminderId}_${dayOfWeek.name}"
    }

    private fun getReminderTag(reminderId: Long): String {
        return "${REMINDER_TAG}_$reminderId"
    }

    companion object {
        private const val REMINDER_TAG = "bp_reminder"
    }
}
