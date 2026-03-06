package com.bptracker.presentation.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bptracker.domain.model.DateRange
import com.bptracker.domain.model.DateRangeType
import com.bptracker.domain.usecase.GetInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val getInsightsUseCase: GetInsightsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        generateInsights()
    }

    fun generateInsights(dateRange: DateRange? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val range = dateRange ?: DateRange.fromType(DateRangeType.MONTH)

            getInsightsUseCase(range).fold(
                onSuccess = { insights ->
                    _uiState.update {
                        it.copy(
                            insights = insights,
                            selectedDateRange = range,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to generate insights"
                        )
                    }
                }
            )
        }
    }

    fun selectDateRange(rangeType: DateRangeType) {
        val dateRange = DateRange.fromType(rangeType)
        generateInsights(dateRange)
    }

    fun retry() {
        generateInsights(_uiState.value.selectedDateRange)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class InsightsUiState(
    val insights: String? = null,
    val selectedDateRange: DateRange = DateRange.fromType(DateRangeType.MONTH),
    val isLoading: Boolean = false,
    val error: String? = null
)
