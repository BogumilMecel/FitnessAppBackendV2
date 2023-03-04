package com.gmail.bogumilmecel2.user.log.routes

import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntry
import io.ktor.server.routing.*

fun Route.configureLogRoutes(
    checkLatestLogEntry: CheckLatestLogEntry
){
    route("/logEntries/"){
        configureGetLatestLogEntryRoute(checkLatestLogEntry)
    }
}