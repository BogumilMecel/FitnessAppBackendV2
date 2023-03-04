package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils

import kotlinx.serialization.Serializable

@Serializable
enum class TimeRequired(val displayValue: String) {
    LOW("15 min"),
    AVERAGE("30 min"),
    HIGH("45 min"),
    MORE("60+ min")
}