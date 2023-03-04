package com.gmail.bogumilmecel2.authentication.domain.use_case

import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUser

data class AuthUseCases(
    val registerNewUserUseCase: RegisterNewUserUseCase,
    val getUserByUsername: GetUserByUsername,
    val getUser: GetUser
)
