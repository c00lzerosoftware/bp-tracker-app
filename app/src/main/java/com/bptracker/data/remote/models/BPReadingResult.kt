package com.bptracker.data.remote.models

data class BPReadingResult(
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val confidence: Float = 0f
)
