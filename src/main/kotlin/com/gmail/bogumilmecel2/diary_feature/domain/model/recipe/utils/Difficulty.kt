package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils

import kotlinx.serialization.Serializable

@Serializable
enum class Difficulty(val displayValue: String) {
    VERY_LOW(displayValue = "1"),
    LOW(displayValue = "2"),
    AVERAGE(displayValue = "3"),
    HIGH(displayValue = "4"),
    VERY_HIGH(displayValue = "5"),
}