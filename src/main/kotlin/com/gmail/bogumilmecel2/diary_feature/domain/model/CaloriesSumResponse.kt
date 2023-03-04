package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CaloriesSumResponse(
    val caloriesSum: Int
)

