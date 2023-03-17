package com.gmail.bogumilmecel2.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Country(val shortName: String) {
    POLAND(shortName = "pl");

    companion object {
        fun getCountryFromString(stringValue: String) = values().find { it.shortName == stringValue }
    }

    fun getCurrency() = when(this) {
        POLAND -> Currency.PLN
    }
}