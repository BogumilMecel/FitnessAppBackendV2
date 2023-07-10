package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.domain.constants.ValidationConstants
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLogEntriesUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import kotlinx.datetime.TimeZone

class CheckIfShouldAskForWeightDialogsUseCase(
    private val getLogEntriesUseCase: GetLogEntriesUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        timeZone: TimeZone
    ): Resource<Unit> {
        val userWeightDialogsResource = userRepository.getWeightDialogsData(userId = userId)

        if (userWeightDialogsResource is Resource.Error) return Resource.Error()

        val userWeightDialogsData = userWeightDialogsResource.data

        if (userWeightDialogsData != null) {
            if (userWeightDialogsData.accepted == true) return Resource.Error()
            if (userWeightDialogsData.lastTimeAsked == CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone = timeZone).toString()) return Resource.Error()
            if (userWeightDialogsData.askedCount >= ValidationConstants.Weight.MINIMUM_ENTRIES_COUNT) return Resource.Error()
        }

        if (!checkIfWeightAndLogEntriesAreValid(userId)) return Resource.Error()

        return Resource.Success(Unit)
    }

    private suspend fun checkIfWeightAndLogEntriesAreValid(userId: String): Boolean {
        val minNumber = ValidationConstants.Weight.MINIMUM_ENTRIES_COUNT

        val latestTwoLogEntries = getLogEntriesUseCase(userId = userId, limit = minNumber).data ?: return false
        if (latestTwoLogEntries.size < minNumber) return false

        val weightEntries = getWeightEntriesUseCase(userId = userId, limit = minNumber).data ?: return false
        if (weightEntries.size < minNumber) return false

        return true
    }
}