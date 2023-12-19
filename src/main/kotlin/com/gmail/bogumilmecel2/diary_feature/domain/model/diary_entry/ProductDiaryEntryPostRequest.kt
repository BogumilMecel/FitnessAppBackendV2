package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ProductDiaryEntryPostRequest(
    val productId: String? = null,
    val weight: Int? = null,
    val mealName: MealName? = null,
    val date: LocalDate? = null,
    val nutritionValues: NutritionValues? = null,
)
