package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class NewPriceRequest(
    val paidHowMuch: Double,
    val paidForWeight: Int,
    val currency: Currency
)