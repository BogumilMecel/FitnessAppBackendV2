package com.gmail.bogumilmecel2.common.util

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    @SerialName("errors")
    val errors: List<Error>? = null,

    @SerialName("message")
    val message: String? = null
)