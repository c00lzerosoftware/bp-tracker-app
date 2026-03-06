package com.bptracker.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bptracker.data.remote.models.BPReadingResult
import com.bptracker.domain.usecase.ScanBPReadingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val scanBPReadingUseCase: ScanBPReadingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun processImage(imageBytes: ByteArray, imageUri: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }

            scanBPReadingUseCase.extractReading(imageBytes).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            scannedResult = result,
                            scannedImageUri = imageUri,
                            isProcessing = false,
                            showConfirmDialog = true,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            error = error.message ?: "Failed to scan reading"
                        )
                    }
                }
            )
        }
    }

    fun confirmAndSaveReading(notes: String? = null) {
        val result = _uiState.value.scannedResult ?: return
        val imageUri = _uiState.value.scannedImageUri

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            scanBPReadingUseCase.saveScannedReading(
                readingResult = result,
                imageUri = imageUri,
                notes = notes
            ).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            showConfirmDialog = false,
                            scannedResult = null,
                            scannedImageUri = null,
                            savedSuccessfully = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = error.message ?: "Failed to save reading"
                        )
                    }
                }
            )
        }
    }

    fun cancelConfirmation() {
        _uiState.update {
            it.copy(
                showConfirmDialog = false,
                scannedResult = null,
                scannedImageUri = null
            )
        }
    }

    fun resetSavedState() {
        _uiState.update { it.copy(savedSuccessfully = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class CameraUiState(
    val scannedResult: BPReadingResult? = null,
    val scannedImageUri: String? = null,
    val isProcessing: Boolean = false,
    val isSaving: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: String? = null
)
