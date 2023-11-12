package com.gmail.bogumilmecel2.authentication.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureIsReachableRoute() {
    get {
        call.respond(true)
    }
}