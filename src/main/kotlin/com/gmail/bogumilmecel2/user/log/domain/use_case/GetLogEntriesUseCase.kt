package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class GetLogEntriesUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        limit: Int
    ): Resource<List<LogEntry>> {
        if (limit > 14 || limit <= 0) return Resource.Error()

        return userRepository.getLogEntries(limit = limit, userId = userId)
    }
}