package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDiaryEntryRequest(
    val recipeId: String,
    val servings: Int,
    val date: LocalDate,
    val mealName: MealName,
    val nutritionValues: NutritionValues
)