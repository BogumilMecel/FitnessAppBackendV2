package com.gmail.bogumilmecel2.common.util

import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    val errors: List<Error>? = null,
    val message: String? = null
)