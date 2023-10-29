package com.gmail.bogumilmecel2.common.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MeasurementUnit {
    @SerialName("grams")
    GRAMS,

    @SerialName("milliliters")
    MILLILITERS;
}