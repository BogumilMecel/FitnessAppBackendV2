package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightDialogs(
    val accepted: Boolean?,
    val lastTimeAsked: String,
    val askedCount: Int
)