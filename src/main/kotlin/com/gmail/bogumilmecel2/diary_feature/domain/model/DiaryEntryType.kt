package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class DiaryEntryType {
    PRODUCT, RECIPE
}