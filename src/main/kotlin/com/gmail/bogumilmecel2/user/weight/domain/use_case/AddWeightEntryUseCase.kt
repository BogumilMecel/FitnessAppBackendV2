package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.CheckIfWeightIsValidUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.NewWeightEntryRequest
import com.gmail.bogumilmecel2.user.weight.domain.model.NewWeightEntryResponse
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import kotlinx.datetime.TimeZone

class AddWeightEntryUseCase(
    private val userRepository: UserRepository,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val getLatestWeightEntryUseCase: GetLatestWeightEntryUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val checkIfWeightIsValidUseCase: CheckIfWeightIsValidUseCase
) {

    suspend operator fun invoke(
        newWeightEntryRequest: NewWeightEntryRequest,
        timeZone: TimeZone,
        userId: String
    ): Resource<NewWeightEntryResponse> {
        if (!checkIfWeightIsValidUseCase(newWeightEntryRequest.value)) return Resource.Error()

        val latestWeightEntryResource = getLatestWeightEntryUseCase(userId = userId)
        val userDate = CustomDateUtils.getTimeZoneDate(timeZone = timeZone) ?: return Resource.Error()
        if (latestWeightEntryResource is Resource.Success) {
            val hasWeightEntryBeenEnteredToday: Boolean = latestWeightEntryResource.data?.date == userDate

            if (!hasWeightEntryBeenEnteredToday) {
                val weightEntry = WeightEntry(
                    creationDateTime = CustomDateUtils.getUtcDateTime(),
                    value = newWeightEntryRequest.value.round(2),
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
                                calculateWeightProgressUseCase(
                                    weightEntries = it,
                                    userId = userId
                                )
                            }
                        )
                    )
                }
            }
        }
        return Resource.Error()
    }
}