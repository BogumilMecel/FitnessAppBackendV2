package com.gmail.bogumilmecel2.user.weight.routes

import com.gmail.bogumilmecel2.common.util.extensions.getTimezoneHeader
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.user.weight.domain.use_case.CheckIfShouldAskForWeightDialogsUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureCheckIfShouldAskForWeightDialogsRoute(
    checkIfShouldAskForWeightDialogsUseCase: CheckIfShouldAskForWeightDialogsUseCase
) {
    authenticate {
        get("/dialogs") {
            call.run {
                getUserId()?.let { userId ->
                    getTimezoneHeader()?.let { timeZone ->
                        handleResource(
                            resource = checkIfShouldAskForWeightDialogsUseCase(
                                userId = userId,
                                timeZone = timeZone
                            )
                        )
                    }
                }
            }
        }
    }
}
