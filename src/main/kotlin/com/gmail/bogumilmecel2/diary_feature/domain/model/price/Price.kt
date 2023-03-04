package com.gmail.bogumilmecel2.diary_feature.domain.model.price

import com.gmail.bogumilmecel2.common.domain.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class Price(
    val value: Double = 0.0,
    val currency: Currency = Currency.PLN
)