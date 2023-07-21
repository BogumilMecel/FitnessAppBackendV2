package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues

data class ProductDiarySearchItem(
    val productId: String,
    val productName: String,
    val nutritionValues: NutritionValues,
    val weight: Int,
    val measurementUnit: MeasurementUnit,
    val timestamp: Long
)