package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class GetLatestLogEntry(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Resource<LogEntry?> {
        return userRepository.getLatestLogEntry(
            userId = userId
        )
    }
}