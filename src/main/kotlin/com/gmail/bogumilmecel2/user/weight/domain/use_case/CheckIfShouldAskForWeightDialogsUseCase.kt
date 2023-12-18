package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.domain.constants.Constants
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
        val user = userRepository.getUser(userId = userId).data ?: return Resource.Error()
        if (user.askForWeightDaily != null) return Resource.Error()
        val weightDialogsQuestions = userRepository.getWeightDialogsQuestions(userId = userId).data ?: return Resource.Error()
        if (weightDialogsQuestions.size > 3) return Resource.Error()
        val currentDate = CustomDateUtils.getTimeZoneDate(timeZone = timeZone)
        if (weightDialogsQuestions.lastOrNull()?.date == currentDate) return Resource.Error()
        if (!checkIfWeightAndLogEntriesAreValid(userId)) return Resource.Error()

        return Resource.Success(Unit)
    }

    private suspend fun checkIfWeightAndLogEntriesAreValid(userId: String): Boolean {
        val minNumber = Constants.Weight.MINIMUM_ENTRIES_COUNT

        val latestTwoLogEntries = getLogEntriesUseCase(userId = userId, limit = minNumber).data ?: return false
        if (latestTwoLogEntries.size < minNumber) return false

        val weightEntries = getWeightEntriesUseCase(userId = userId, limit = minNumber).data ?: return false
        if (weightEntries.size < minNumber) return false

        return true
    }
}