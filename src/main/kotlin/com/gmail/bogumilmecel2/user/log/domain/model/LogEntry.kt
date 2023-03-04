package com.gmail.bogumilmecel2.user.log.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LogEntry(
    val streak:Int = 1,
    val timestamp:Long = System.currentTimeMillis()
)
