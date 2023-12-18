package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class GetLatestWeightEntryUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String) = userRepository.getLatestWeightEntry(userId = userId)
}