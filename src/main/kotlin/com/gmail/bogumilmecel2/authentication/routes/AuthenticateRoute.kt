package com.gmail.bogumilmecel2.authentication.routes

import com.gmail.bogumilmecel2.common.util.extensions.getTimezoneHeader
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUserUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAuthenticateRoute(
    getUserUseCase: GetUserUseCase
) {
    authenticate {
        post("/authenticate") {
            call.run {
                getUserId()?.let { userId ->
                    getTimezoneHeader()?.let { timezone ->
                        handleResource(
                            resource = getUserUseCase(
                                timezone = timezone,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}