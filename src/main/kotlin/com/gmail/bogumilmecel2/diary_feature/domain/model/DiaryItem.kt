package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface DiaryItem {
    val id: String
    val nutritionValues: NutritionValues
    val utcTimestamp: Long
    val userId: String
    val date: LocalDate?
    val mealName: MealName
    val creationDate: LocalDateTime?
    val changeDate: LocalDateTime?
}