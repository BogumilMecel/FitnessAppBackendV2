package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import kotlinx.serialization.Serializable

@Serializable
data class DiaryEntriesResponse(
    val productDiaryEntries: List<ProductDiaryEntry>,
    val recipeDiaryEntries: List<RecipeDiaryEntry>
)