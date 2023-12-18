package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.use_case.CalculateWeightProgressUseCase
import com.gmail.bogumilmecel2.user.weight.domain.use_case.GetWeightEntriesUseCase
import kotlinx.datetime.TimeZone

class GetUser(
    private val userRepository: UserRepository,
    private val checkLatestLogEntry: CheckLatestLogEntry,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase
) {
    suspend operator fun invoke(userId: String, timezone: TimeZone): Resource<User?>{
        val user = userRepository.getUser(userId = userId).data ?: return Resource.Error()

        val logEntryResource = checkLatestLogEntry(
            userId = userId,
            timezone = timezone
        )

        val weightEntriesResource = getWeightEntriesUseCase(
            userId = userId,
            limit = 14
        )

        val weightProgress: String? = weightEntriesResource.data?.let {
            calculateWeightProgressUseCase(it)
        }

        return Resource.Success(
            data = user.copy(
                latestLogEntry = if (logEntryResource is Resource.Success) logEntryResource.data else user.latestLogEntry,
                latestWeightEntry = weightEntriesResource.data?.getOrNull(0),
                weightProgress = weightProgress
            )
        )
    }
}