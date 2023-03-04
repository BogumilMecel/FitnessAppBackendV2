package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NewWeightEntryResponse(
    val weightProgress: String? = null,
    val latestWeightEntry: WeightEntry? = null
)
