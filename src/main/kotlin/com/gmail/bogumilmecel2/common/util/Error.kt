package com.gmail.bogumilmecel2.common.util

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val description: String? = null,
    val field: String? = null,
)