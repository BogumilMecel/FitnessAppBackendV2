package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.WeightDialogsData
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntryAndGetLogStreakUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsLastTimeAsked
import com.gmail.bogumilmecel2.user.weight.domain.use_case.CalculateWeightProgressUseCase
import com.gmail.bogumilmecel2.user.weight.domain.use_case.GetWeightEntriesUseCase
import kotlinx.datetime.TimeZone

class GetUserUseCase(
    private val checkLatestLogEntryAndGetLogStreakUseCase: CheckLatestLogEntryAndGetLogStreakUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val getUserObjectUseCase: GetUserObjectUseCase,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, timezone: TimeZone): Resource<User?> {
        val user = getUserObjectUseCase(userId = userId).data ?: return Resource.Error()

        val logEntryResource = checkLatestLogEntryAndGetLogStreakUseCase(
            userId = userId,
            timeZone = timezone,
            userStreak = user.logStreak
        )

        val weightEntriesResource = getWeightEntriesUseCase(
            userId = userId,
            limit = 14
        )

        val weightProgress: String? = weightEntriesResource.data?.let {
            calculateWeightProgressUseCase(it)
        }

        val weightDialogsResource = userRepository.getWeightDialogsData(
            userId = userId
        )

        val weightDialogsData = when (weightDialogsResource) {
            is Resource.Success -> {
                weightDialogsResource.data?.let {
                    WeightDialogsData(
                        accepted = it.accepted,
                        lastTimeAsked = WeightDialogsLastTimeAsked(
                            lastTimeAsked = it.lastTimeAsked,
                            askedCount = it.askedCount
                        )
                    )
                }
            }

            is Resource.Error -> {
                null
            }
        }

        return Resource.Success(
            data = user.copy(
                logStreak = if (logEntryResource is Resource.Success) logEntryResource.data else 1,
                latestWeightEntry = weightEntriesResource.data?.getOrNull(0),
                weightProgress = weightProgress,
                weightDialogsData = weightDialogsData
            )
        )
    }
}