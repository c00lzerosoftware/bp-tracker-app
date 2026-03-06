package com.bptracker.data.repository

import com.bptracker.data.local.Reminder
import com.bptracker.data.local.ReminderDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) {

    fun getAllReminders(): Flow<List<Reminder>> =
        reminderDao.getAllRemindersFlow()

    suspend fun getReminderById(id: Long): Reminder? =
        reminderDao.getReminderById(id)

    fun getEnabledReminders(): Flow<List<Reminder>> =
        reminderDao.getEnabledRemindersFlow()

    suspend fun getEnabledRemindersList(): List<Reminder> =
        reminderDao.getEnabledReminders()

    suspend fun insertReminder(reminder: Reminder): Long =
        reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) =
        reminderDao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) =
        reminderDao.deleteReminder(reminder)

    suspend fun deleteReminderById(id: Long) =
        reminderDao.deleteReminderById(id)

    suspend fun setReminderEnabled(id: Long, isEnabled: Boolean) =
        reminderDao.setReminderEnabled(id, isEnabled)
}
