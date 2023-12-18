package com.gmail.bogumilmecel2.user.user_data.routes

import com.gmail.bogumilmecel2.common.util.extensions.getTimezoneHeader
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.user.user_data.domain.model.IntroductionRequest
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.HandleUserInformationUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureHandleUserInformationRoute(handleUserInformationUseCase: HandleUserInformationUseCase) {
    authenticate {
        post("/userInformation/") {
            call.run {
                receiveOrRespond<IntroductionRequest>()?.let { request ->
                    getTimezoneHeader()?.let { timezone ->
                        getUserId()?.let { userId ->
                            call.handleResource(
                                resource = handleUserInformationUseCase(
                                    introductionRequest = request,
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
}