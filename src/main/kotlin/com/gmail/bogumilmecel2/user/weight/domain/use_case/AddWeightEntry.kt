package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.NewWeightEntryResponse
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import org.apache.commons.lang3.time.DateUtils
import java.util.*

class AddWeightEntry(
    private val userRepository: UserRepository,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase
) {

    suspend operator fun invoke(
        weightEntry: WeightEntry,
        userId: String
    ): Resource<NewWeightEntryResponse> {
        val latestWeightEntryResource = getWeightEntriesUseCase(limit = 1, userId = userId)
        if (latestWeightEntryResource is Resource.Success) {
            val hasWeightEntryBeenEnteredToday: Boolean = latestWeightEntryResource.data.getOrNull(0)?.let { latestWeightEntry ->
                DateUtils.isSameDay(
                    Date(latestWeightEntry.timestamp),
                    Date(weightEntry.timestamp)
                )
            } ?: false

            if (!hasWeightEntryBeenEnteredToday) {
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