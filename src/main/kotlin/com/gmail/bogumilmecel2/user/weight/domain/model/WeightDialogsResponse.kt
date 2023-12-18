package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightDialogsResponse(
    val shouldShowWeightPicker: Boolean,
    val weightDialogsLastTimeAsked: WeightDialogsLastTimeAsked?
)