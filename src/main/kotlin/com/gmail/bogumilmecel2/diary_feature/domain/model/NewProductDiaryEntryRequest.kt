package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewProductDiaryEntryRequest(
    @SerialName("product_id")
    val productId: String,

    @SerialName("weight")
    val weight: Int,

    @SerialName("date")
    val date: LocalDate,

    @SerialName("meal_name")
    val mealName: MealName,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues,
)
