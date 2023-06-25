package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductDiaryEntryPostRequest(
    val product: Product,
    val weight: Int,
    val mealName: MealName,
    val date: String
)
