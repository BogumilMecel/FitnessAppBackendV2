package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import kotlinx.serialization.Serializable

@Serializable
data class EditProductDiaryEntryRequest(
    val productDiaryEntry: ProductDiaryEntry,
    val newWeight: Int
)
