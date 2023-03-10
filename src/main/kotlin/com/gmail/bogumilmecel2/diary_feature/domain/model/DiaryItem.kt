package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues

interface DiaryItem {
    val id: String
    val nutritionValues: NutritionValues
    val timestamp: Long
    val userId: String
    val date: String
    val mealName: MealName
}