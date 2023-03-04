package com.gmail.bogumilmecel2.authentication.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest(
    val timestamp: Long
)
