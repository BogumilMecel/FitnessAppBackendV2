package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class WeightDialogsQuestion(
    val date: LocalDate,
    val userId: String
)