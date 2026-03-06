package com.bptracker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bptracker.data.local.Reminder
import com.bptracker.domain.usecase.ManageRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val manageRemindersUseCase: ManageRemindersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadReminders()
    }

    private fun loadReminders() {
        viewModelScope.launch {
            manageRemindersUseCase.getAllReminders()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { reminders ->
                    _uiState.update {
                        it.copy(
                            reminders = reminders,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun addReminder(
        time: LocalTime,
        daysOfWeek: Set<DayOfWeek>,
        label: String? = null
    ) {
        viewModelScope.launch {
            manageRemindersUseCase.addReminder(time, daysOfWeek, label).fold(
                onSuccess = {
                    _uiState.update { it.copy(showAddReminderDialog = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun deleteReminder(reminderId: Long) {
        viewModelScope.launch {
            manageRemindersUseCase.deleteReminder(reminderId).fold(
                onSuccess = {
                    _uiState.update { it.copy(error = null) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun toggleReminder(reminderId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            manageRemindersUseCase.toggleReminder(reminderId, isEnabled).fold(
                onSuccess = {
                    _uiState.update { it.copy(error = null) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun showAddReminderDialog() {
        _uiState.update { it.copy(showAddReminderDialog = true) }
    }

    fun hideAddReminderDialog() {
        _uiState.update { it.copy(showAddReminderDialog = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class SettingsUiState(
    val reminders: List<Reminder> = emptyList(),
    val showAddReminderDialog: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)
