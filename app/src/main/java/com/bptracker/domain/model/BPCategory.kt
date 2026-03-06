package com.bptracker.domain.model

enum class BPCategory(
    val displayName: String,
    val systolicRange: IntRange,
    val diastolicRange: IntRange
) {
    NORMAL(
        displayName = "Normal",
        systolicRange = 0..120,
        diastolicRange = 0..80
    ),
    ELEVATED(
        displayName = "Elevated",
        systolicRange = 121..129,
        diastolicRange = 0..80
    ),
    HYPERTENSION_STAGE_1(
        displayName = "Hypertension Stage 1",
        systolicRange = 130..139,
        diastolicRange = 80..89
    ),
    HYPERTENSION_STAGE_2(
        displayName = "Hypertension Stage 2",
        systolicRange = 140..179,
        diastolicRange = 90..119
    ),
    HYPERTENSIVE_CRISIS(
        displayName = "Hypertensive Crisis",
        systolicRange = 180..300,
        diastolicRange = 120..200
    );

    companion object {
        fun fromReading(systolic: Int, diastolic: Int): BPCategory {
            return when {
                systolic >= 180 || diastolic >= 120 -> HYPERTENSIVE_CRISIS
                systolic >= 140 || diastolic >= 90 -> HYPERTENSION_STAGE_2
                systolic >= 130 || diastolic >= 80 -> HYPERTENSION_STAGE_1
                systolic >= 121 -> ELEVATED
                else -> NORMAL
            }
        }
    }
}
