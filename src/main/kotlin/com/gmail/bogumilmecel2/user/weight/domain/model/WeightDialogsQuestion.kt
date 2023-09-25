package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightDialogsQuestion(
    val date: String,
    val userId: String
)