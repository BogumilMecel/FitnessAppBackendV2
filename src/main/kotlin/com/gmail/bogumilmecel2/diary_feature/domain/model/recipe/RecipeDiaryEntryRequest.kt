package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

data class RecipeDiaryEntryRequest(
    val recipe: Recipe,
    val servings: Int,
    val timestamp: Long,
    val date: String,
    val mealName: String
)
