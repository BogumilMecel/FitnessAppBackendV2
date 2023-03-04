package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class InsertLogEntry(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(userId: String, newLogEntry: LogEntry): Resource<LogEntry> {
        return userRepository.saveLogEntry(entry = newLogEntry, userId = userId)
    }
}