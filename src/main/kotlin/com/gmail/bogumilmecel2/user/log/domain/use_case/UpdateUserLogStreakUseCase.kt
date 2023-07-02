package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class UpdateUserLogStreakUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        streak: Int
    ): Resource<Unit> {
        return userRepository.updateUserLogStreak(
            userId = userId,
            streak = streak
        )
    }
}