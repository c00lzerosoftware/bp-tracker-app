package com.bptracker.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bptracker.data.local.BPReading
import com.bptracker.data.local.ReadingSource
import com.bptracker.domain.model.BPCategory
import com.bptracker.domain.usecase.AddBPReadingUseCase
import com.bptracker.domain.usecase.GetBPReadingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBPReadingsUseCase: GetBPReadingsUseCase,
    private val addBPReadingUseCase: AddBPReadingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRecentReadings()
    }

    private fun loadRecentReadings() {
        viewModelScope.launch {
            getBPReadingsUseCase.getRecentReadings(limit = 10)
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { readings ->
                    _uiState.update {
                        it.copy(
                            recentReadings = readings,
                            latestReading = readings.firstOrNull(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun addManualReading(
        systolic: Int,
        diastolic: Int,
        pulse: Int,
        notes: String? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            addBPReadingUseCase(
                systolic = systolic,
                diastolic = diastolic,
                pulse = pulse,
                notes = notes,
                source = ReadingSource.MANUAL
            ).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            showAddReadingDialog = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    fun showAddReadingDialog() {
        _uiState.update { it.copy(showAddReadingDialog = true) }
    }

    fun hideAddReadingDialog() {
        _uiState.update { it.copy(showAddReadingDialog = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class HomeUiState(
    val recentReadings: List<BPReading> = emptyList(),
    val latestReading: BPReading? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val showAddReadingDialog: Boolean = false,
    val error: String? = null
) {
    val latestCategory: BPCategory?
        get() = latestReading?.let {
            BPCategory.fromReading(it.systolic, it.diastolic)
        }
}
