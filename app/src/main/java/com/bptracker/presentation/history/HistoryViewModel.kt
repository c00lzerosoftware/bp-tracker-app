package com.bptracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bptracker.data.local.BPReading
import com.bptracker.domain.model.DateRange
import com.bptracker.domain.model.DateRangeType
import com.bptracker.domain.usecase.DeleteBPReadingUseCase
import com.bptracker.domain.usecase.GetBPReadingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getBPReadingsUseCase: GetBPReadingsUseCase,
    private val deleteBPReadingUseCase: DeleteBPReadingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadReadings()
    }

    private fun loadReadings() {
        viewModelScope.launch {
            val dateRange = _uiState.value.selectedDateRange

            getBPReadingsUseCase.getReadingsByDateRange(dateRange)
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
                            readings = readings,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun selectDateRange(rangeType: DateRangeType) {
        _uiState.update {
            it.copy(
                selectedDateRange = DateRange.fromType(rangeType),
                isLoading = true
            )
        }
        loadReadings()
    }

    fun deleteReading(readingId: Long) {
        viewModelScope.launch {
            deleteBPReadingUseCase(readingId).fold(
                onSuccess = {
                    _uiState.update { it.copy(error = null) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class HistoryUiState(
    val readings: List<BPReading> = emptyList(),
    val selectedDateRange: DateRange = DateRange.fromType(DateRangeType.MONTH),
    val isLoading: Boolean = true,
    val error: String? = null
)
