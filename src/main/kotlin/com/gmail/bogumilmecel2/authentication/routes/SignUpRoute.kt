package com.gmail.bogumilmecel2.authentication.routes

import com.gmail.bogumilmecel2.authentication.domain.model.RegisterRequest
import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.use_case.RegisterNewUserUseCase
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureSignUpRoute(
    registerNewUserUseCase: RegisterNewUserUseCase,
    tokenConfig: TokenConfig
) {
    post("/signup/") {
        call.run {
            receiveOrRespond<RegisterRequest>()?.let { request ->
                handleResource(
                    resource = registerNewUserUseCase(
                        email = request.email,
                        password = request.password,
                        username = request.username,
                        tokenConfig = tokenConfig
                    )
                )
            }
        }
    }
}