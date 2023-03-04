package com.gmail.bogumilmecel2.common.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            "Status: $status, HTTP method: $httpMethod ${call.request.path()} ${call.request.contentType().contentType}"
        }
    }
}
