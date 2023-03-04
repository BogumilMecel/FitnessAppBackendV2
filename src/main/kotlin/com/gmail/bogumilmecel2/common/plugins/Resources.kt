package com.gmail.bogumilmecel2.common.plugins

import io.ktor.server.application.*
import io.ktor.server.resources.*

fun Application.configureLocations(){
    install(Resources)
}