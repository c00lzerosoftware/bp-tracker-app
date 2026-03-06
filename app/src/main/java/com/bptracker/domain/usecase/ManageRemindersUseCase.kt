package com.bptracker.domain.usecase

import com.bptracker.data.local.Reminder
import com.bptracker.data.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

class ManageRemindersUseCase @Inject constructor(
    private val repository: ReminderRepository
) {

    fun getAllReminders(): Flow<List<Reminder>> {
        return repository.getAllReminders()
    }

    fun getEnabledReminders(): Flow<List<Reminder>> {
        return repository.getEnabledReminders()
    }

    suspend fun addReminder(
        time: LocalTime,
        daysOfWeek: Set<DayOfWeek>,
        label: String? = null
    ): Result<Long> {
        return try {
            val reminder = Reminder(
                time = time,
                daysOfWeek = daysOfWeek,
                isEnabled = true,
                label = label
            )
            val id = repository.insertReminder(reminder)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReminder(reminder: Reminder): Result<Unit> {
        return try {
            repository.updateReminder(reminder)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReminder(reminderId: Long): Result<Unit> {
        return try {
            repository.deleteReminderById(reminderId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleReminder(reminderId: Long, isEnabled: Boolean): Result<Unit> {
        return try {
            repository.setReminderEnabled(reminderId, isEnabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
