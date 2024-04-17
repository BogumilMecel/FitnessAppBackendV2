package com.gmail.bogumilmecel2.authentication.domain.use_case

import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUserUseCase

data class AuthUseCases(
    val registerNewUserUseCase: RegisterNewUserUseCase,
    val getUserByUsernameUseCase: GetUserByUsernameUseCase,
    val getUserUseCase: GetUserUseCase
)
