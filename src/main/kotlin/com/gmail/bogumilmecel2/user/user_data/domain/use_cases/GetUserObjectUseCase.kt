package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class GetUserObjectUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): Resource<User?> {
        return userRepository.getUser(userId = userId)
    }
}