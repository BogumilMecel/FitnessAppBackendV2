package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EditRecipeDiaryEntryRequest(
    val recipeDiaryEntryId: String,
    val newServings: Int
)
