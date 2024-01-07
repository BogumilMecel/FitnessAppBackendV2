package com.gmail.bogumilmecel2.common.domain.use_case

import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class CheckIfUserExistsUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): Boolean {
        return userRepository.getUser(userId = userId).data != null
    }
}