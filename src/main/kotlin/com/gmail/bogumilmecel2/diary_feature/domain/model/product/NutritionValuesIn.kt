package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NutritionValuesIn {
    @SerialName("hundred_grams")
    HUNDRED_GRAMS,

    @SerialName("container")
    CONTAINER,

    @SerialName("average")
    AVERAGE;
}