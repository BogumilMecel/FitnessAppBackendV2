package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import kotlinx.serialization.Serializable

@Serializable
data class NewPriceRequest(
    val paidHowMuch: Double,
    val paidForWeight: Int
)