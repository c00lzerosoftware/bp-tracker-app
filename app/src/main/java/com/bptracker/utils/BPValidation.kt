package com.bptracker.utils

object BPValidation {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateSystolic(systolic: Int): ValidationResult {
        return when {
            systolic < 70 -> ValidationResult(false, "Systolic too low (minimum 70 mmHg)")
            systolic > 250 -> ValidationResult(false, "Systolic too high (maximum 250 mmHg)")
            else -> ValidationResult(true)
        }
    }

    fun validateDiastolic(diastolic: Int): ValidationResult {
        return when {
            diastolic < 40 -> ValidationResult(false, "Diastolic too low (minimum 40 mmHg)")
            diastolic > 150 -> ValidationResult(false, "Diastolic too high (maximum 150 mmHg)")
            else -> ValidationResult(true)
        }
    }

    fun validatePulse(pulse: Int): ValidationResult {
        return when {
            pulse < 40 -> ValidationResult(false, "Pulse too low (minimum 40 bpm)")
            pulse > 200 -> ValidationResult(false, "Pulse too high (maximum 200 bpm)")
            else -> ValidationResult(true)
        }
    }

    fun validateReading(systolic: Int, diastolic: Int, pulse: Int): ValidationResult {
        validateSystolic(systolic).let { if (!it.isValid) return it }
        validateDiastolic(diastolic).let { if (!it.isValid) return it }
        validatePulse(pulse).let { if (!it.isValid) return it }

        if (systolic <= diastolic) {
            return ValidationResult(false, "Systolic must be greater than diastolic")
        }

        return ValidationResult(true)
    }
}
