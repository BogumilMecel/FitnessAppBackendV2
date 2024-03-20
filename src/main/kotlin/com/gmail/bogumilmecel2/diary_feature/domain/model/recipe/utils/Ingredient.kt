package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val weight: Int,
    val productName: String,
    val nutritionValues: NutritionValues,
    val productId: String,
    val measurementUnit: MeasurementUnit,
)
