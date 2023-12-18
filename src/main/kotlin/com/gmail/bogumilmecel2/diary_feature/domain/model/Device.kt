package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val id: String,
    val createdUtcTimestamp: Long,
    val lastLoggedInUtcTimestamp: Long,
    val lastLoggedInDate: String
)