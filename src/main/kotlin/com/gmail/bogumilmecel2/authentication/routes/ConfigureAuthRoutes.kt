package com.gmail.bogumilmecel2.authentication.routes

import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.use_case.AuthUseCases
import io.ktor.server.routing.*

fun Route.configureAuthRoutes(
    authUseCases: AuthUseCases,
    tokenConfig: TokenConfig
){
    route("/authentication/"){
        configureSignUpRoute(
            registerNewUserUseCase = authUseCases.registerNewUserUseCase,
            tokenConfig = tokenConfig
        )
        configureSignInRoute(
            getUserByUsernameUseCase = authUseCases.getUserByUsernameUseCase,
            tokenConfig = tokenConfig
        )
        configureAuthenticateRoute(authUseCases.getUserUseCase)
    }
}

