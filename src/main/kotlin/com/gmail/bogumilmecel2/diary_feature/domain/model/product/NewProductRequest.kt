package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.serialization.Serializable

@Serializable
data class NewProductRequest(
    val name: String,
    val measurementUnit: MeasurementUnit,
    val containerWeight: Int? = null,
    val barcode: String? = null,
    val nutritionValuesIn: NutritionValuesIn,
    val nutritionValues: NutritionValues
)