package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GetDiaryEntriesRequest(
    val latestProductDiaryEntryUtcTimestamp: Long?,
    val latestRecipeDiaryEntryUtcTimestamp: Long?
)