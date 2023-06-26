package com.gmail.bogumilmecel2.user.log.routes

import com.gmail.bogumilmecel2.common.util.extensions.getTimezoneHeader
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntry
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetLatestLogEntryRoute(
    checkLatestLogEntry: CheckLatestLogEntry
) {
    authenticate {
        post("/latest/") {
            call.run {
                getTimezoneHeader()?.let { timezone ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = checkLatestLogEntry(
                                userId = userId,
                                timezone = timezone
                            )
                        )
                    }
                }
            }
        }
    }
}