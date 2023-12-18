package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDiaryEntryPostRequest(
    val productId: String,
    val weight: Int,
    val mealName: MealName,
    val date: String
)
