package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.CheckIfWeightIsValidUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.NewWeightEntryRequest
import com.gmail.bogumilmecel2.user.weight.domain.model.NewWeightEntryResponse
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import kotlinx.datetime.TimeZone

class AddWeightEntryUseCase(
    private val userRepository: UserRepository,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val checkIfWeightIsValidUseCase: CheckIfWeightIsValidUseCase
) {

    suspend operator fun invoke(
        newWeightEntryRequest: NewWeightEntryRequest,
        timeZone: TimeZone,
        userId: String
    ): Resource<NewWeightEntryResponse> {
        if (!checkIfWeightIsValidUseCase(newWeightEntryRequest.value)) return Resource.Error()

        val latestWeightEntryResource = getWeightEntriesUseCase(limit = 1, userId = userId)
        val userDate = CustomDateUtils.getCurrentTimeZoneLocalDateString(timeZone = timeZone)
        if (latestWeightEntryResource is Resource.Success) {
            val hasWeightEntryBeenEnteredToday: Boolean =
                latestWeightEntryResource.data.getOrNull(0)?.date == userDate

            if (!hasWeightEntryBeenEnteredToday) {
                val weightEntry = WeightEntry(
                    utcTimestamp = CustomDateUtils.getCurrentUtcTimestamp(),
                    value = newWeightEntryRequest.value,
                    date = userDate
                )

                val weightEntryResource = userRepository.addWeightEntry(
                    userId = userId,
                    weightEntry = weightEntry
                )

                if (weightEntryResource.data == true) {
                    val weightEntries = getWeightEntriesUseCase(
                        limit = 14,
                        userId = userId
                    )

                    return Resource.Success(
                        data = NewWeightEntryResponse(
                            latestWeightEntry = weightEntry,
                            weightProgress = weightEntries.data?.let {
                                calculateWeightProgressUseCase(it)
                            }
                        )
                    )
                }
            }
        }
        return Resource.Error()
    }
}