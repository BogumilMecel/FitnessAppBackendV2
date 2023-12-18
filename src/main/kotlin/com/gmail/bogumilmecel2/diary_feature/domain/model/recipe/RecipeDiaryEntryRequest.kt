package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDiaryEntryRequest(
    val recipeId: String,
    val servings: Int,
    val date: String,
    val mealName: MealName
)