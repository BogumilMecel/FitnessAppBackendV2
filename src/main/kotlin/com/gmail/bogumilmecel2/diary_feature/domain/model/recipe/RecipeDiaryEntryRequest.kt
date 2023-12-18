package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDiaryEntryRequest(
    val recipe: Recipe,
    val servings: Int,
    val timestamp: Long,
    val date: String,
    val mealName: MealName
)
