package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipePriceResponse(
    val totalPrice: Double,
    val shouldShowPriceWarning: Boolean
)
