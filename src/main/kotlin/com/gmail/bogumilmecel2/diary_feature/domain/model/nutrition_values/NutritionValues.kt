package com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values

import kotlinx.serialization.Serializable

@Serializable
data class NutritionValues(
    val calories:Int = 0,
    val carbohydrates:Double = 0.0,
    val protein:Double = 0.0,
    val fat:Double = 0.0
)
