package com.gmail.bogumilmecel2.user.weight.domain.model

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import kotlinx.serialization.Serializable

@Serializable
data class WeightDialogsResponse(
    val user: User,
    val shouldShowWeightPicker: Boolean
)