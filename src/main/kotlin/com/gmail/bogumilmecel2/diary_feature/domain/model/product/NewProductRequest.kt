package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewProductRequest(
    @SerialName("name")
    val name: String,

    @SerialName("container_weight")
    val containerWeight: Int? = null,

    @SerialName("nutrition_values_in")
    val nutritionValuesIn: NutritionValuesIn,

    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnit,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues,

    @SerialName("barcode")
    val barcode: String? = null,
)