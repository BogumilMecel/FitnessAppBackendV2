package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues

data class RecipeDiaryEntry(
    override val id: String,
    override val nutritionValues: NutritionValues,
    override val timestamp: Long,
    override val userId: String,
    override val date: String,
    override val mealName: String,
    val recipe: Recipe,
    val portions: Int,
) : DiaryItem
