package com.gmail.bogumilmecel2.authentication.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class CheckIfUsernameExists(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(username: String): Resource<Boolean>{
        return userRepository.checkIfUsernameExists(username = username)
    }
}