package com.bptracker.domain.usecase

import com.bptracker.data.repository.BPReadingRepository
import javax.inject.Inject

class DeleteBPReadingUseCase @Inject constructor(
    private val repository: BPReadingRepository
) {

    suspend operator fun invoke(readingId: Long): Result<Unit> {
        return try {
            repository.deleteReadingById(readingId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
