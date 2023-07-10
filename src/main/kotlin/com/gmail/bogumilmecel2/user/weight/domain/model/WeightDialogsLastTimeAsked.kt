package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightDialogsLastTimeAsked(
    val lastTimeAsked: String?,
    val askedCount: Int = 0
)
