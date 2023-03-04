package com.gmail.bogumilmecel2.authentication.routes

import com.gmail.bogumilmecel2.authentication.domain.model.LoginRequest
import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.use_case.GetUserByUsername
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureSignInRoute(
    getUserByUsername: GetUserByUsername,
    tokenConfig: TokenConfig
) {
    post("/signin/") {
        call.run {
            receiveOrRespond<LoginRequest>()?.let { request ->
                handleResource(
                    resource = getUserByUsername(
                        email = request.email,
                        password = request.password,
                        tokenConfig = tokenConfig
                    )
                )
            }
        }
    }
}