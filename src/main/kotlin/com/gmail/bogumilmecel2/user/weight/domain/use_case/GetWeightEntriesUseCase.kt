package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry

class GetWeightEntriesUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        limit: Int
    ): Resource<List<WeightEntry>> {
        return userRepository.getWeightEntries(
            userId = userId,
            limit = limit
        )
    }
}