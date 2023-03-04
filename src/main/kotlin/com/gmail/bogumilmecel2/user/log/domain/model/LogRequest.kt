package com.gmail.bogumilmecel2.user.log.domain.model

@kotlinx.serialization.Serializable
data class LogRequest(
    val timestamp: Long = System.currentTimeMillis()
)