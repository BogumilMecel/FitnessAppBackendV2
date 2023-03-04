package com.gmail.bogumilmecel2.authentication.routes

import com.gmail.bogumilmecel2.authentication.domain.model.user.AuthenticationRequest
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUser
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAuthenticateRoute(
    getUser: GetUser
) {
    authenticate {
        post("/authenticate") {
            call.run {
                getUserId()?.let { userId ->
                    receiveOrRespond<AuthenticationRequest>()?.let { request ->
                        handleResource(
                            resource = getUser(
                                timestamp = request.timestamp,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}