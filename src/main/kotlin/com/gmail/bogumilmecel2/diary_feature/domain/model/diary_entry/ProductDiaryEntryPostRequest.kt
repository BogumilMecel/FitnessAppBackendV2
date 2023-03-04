package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product

data class ProductDiaryEntryPostRequest(
    val product: Product,
    val timestamp: Long,
    val weight: Int,
    val mealName: String,
    val date: String
)
