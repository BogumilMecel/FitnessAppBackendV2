package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface DiaryItem {
    val id: String?
    val nutritionValues: NutritionValues?
    val userId: String?
    val date: LocalDate?
    val mealName: MealName?
    val creationDateTime: LocalDateTime?
    val changeDateTime: LocalDateTime?
}