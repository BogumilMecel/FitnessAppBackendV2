package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import org.bson.types.ObjectId

data class RecipeDiaryEntryDto(
    val _id: ObjectId,
    val nutritionValues: NutritionValues,
    val timestamp: Long,
    val recipe: Recipe,
    val portions: Int,
    val userId: ObjectId,
    val date: String,
    val mealName: String,
)
