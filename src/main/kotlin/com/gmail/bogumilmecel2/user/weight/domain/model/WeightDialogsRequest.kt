package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class WeightDialogsRequest(
    val answer: WeightDialogsAnswer
)

@Serializable
enum class WeightDialogsAnswer {
    Accepted, Declined, NotNow
}