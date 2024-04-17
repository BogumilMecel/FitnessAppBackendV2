package com.gmail.bogumilmecel2.authentication.routes

import com.gmail.bogumilmecel2.authentication.domain.model.AuthResponse
import com.gmail.bogumilmecel2.authentication.domain.model.LoginRequest
import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.use_case.GetUserByUsernameUseCase
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureSignInRoute(
    getUserByUsernameUseCase: GetUserByUsernameUseCase,
    tokenConfig: TokenConfig
) {
    post("/signin/") {
        call.run {
            receiveOrRespond<LoginRequest>()?.let { request ->
                handleResource<AuthResponse>(
                    resource = getUserByUsernameUseCase(
                        email = request.email,
                        password = request.password,
                        tokenConfig = tokenConfig,
                    )
                )
            }
        }
    }
}