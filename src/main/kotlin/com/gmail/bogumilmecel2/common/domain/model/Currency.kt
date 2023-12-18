package com.gmail.bogumilmecel2.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Currency(val shortName: String) {
    PLN(shortName = "pln"), USD(shortName = "usd"), EUR(shortName = "eur");

    companion object {
        fun getCurrencyFromString(stringValue: String) = Currency.values().find { it.shortName == stringValue }
    }
}

fun Double.convertFromDefaultCurrency(currency: Currency): Double {
    return when (currency) {
        Currency.USD -> this
        else -> this
    }
}

fun Double.convertToDefaultCurrency(from: Currency): Double {
    return when (from) {
        else -> this
    }
}