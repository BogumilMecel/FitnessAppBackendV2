package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.domain.constants.ValidationConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLogEntriesUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUserObjectUseCase

class CheckIfShouldAskForWeightDialogsUseCase(
    private val getUserObjectUseCase: GetUserObjectUseCase,
    private val getLogEntriesUseCase: GetLogEntriesUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase
) {
    suspend operator fun invoke(
        userId: String
    ): Resource<Unit> {
        val user = getUserObjectUseCase(userId = userId).data ?: return Resource.Error()

        if (user.askedForWeightDialogs) return Resource.Error()

        val minNumber = ValidationConstants.Weight.MINIMUM_ENTRIES_COUNT

        val latestTwoLogEntries = getLogEntriesUseCase(userId = userId, limit = minNumber).data ?: return Resource.Error()
        if (latestTwoLogEntries.size < minNumber) return Resource.Error()

        val weightEntries = getWeightEntriesUseCase(userId = userId, limit = minNumber).data ?: return Resource.Error()
        if (weightEntries.size < minNumber) return Resource.Error()

        return Resource.Success(Unit)
    }
}