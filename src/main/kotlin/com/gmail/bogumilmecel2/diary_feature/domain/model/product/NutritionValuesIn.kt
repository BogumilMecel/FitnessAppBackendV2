package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import kotlinx.serialization.Serializable

@Serializable
enum class NutritionValuesIn {
    HUNDRED_GRAMS, CONTAINER, AVERAGE;
}