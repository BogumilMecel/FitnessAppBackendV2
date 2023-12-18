package com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NutritionValues(
    @SerialName("calories")
    val calories:Int = 0,

    @SerialName("carbohydrates")
    val carbohydrates:Double = 0.0,

    @SerialName("protein")
    val protein:Double = 0.0,

    @SerialName("fat")
    val fat:Double = 0.0
)