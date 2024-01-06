package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.domain.model.exceptions.InvalidWeightValueException
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.CheckIfWeightIsValidUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import kotlinx.datetime.TimeZone
import org.bson.types.ObjectId

class AddWeightEntryUseCase(
    private val userRepository: UserRepository,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val getLatestWeightEntryUseCase: GetLatestWeightEntryUseCase,
    private val checkIfWeightIsValidUseCase: CheckIfWeightIsValidUseCase
) {

    suspend operator fun invoke(
        weightEntry: WeightEntry,
        timeZone: TimeZone,
        userId: String
    ): Resource<User> = with(weightEntry) {
        value ?: return Resource.Error(InvalidWeightValueException)

        if (!checkIfWeightIsValidUseCase(value)) return Resource.Error(InvalidWeightValueException)

        val latestWeightEntryResource = getLatestWeightEntryUseCase(userId = userId)
        val userDate = CustomDateUtils.getTimeZoneDate(timeZone = timeZone) ?: return Resource.Error()

        if (latestWeightEntryResource is Resource.Success) {
            val hasWeightEntryBeenEnteredToday: Boolean = latestWeightEntryResource.data?.date == userDate

            if (!hasWeightEntryBeenEnteredToday) {
                val newWeightEntry = WeightEntryDto(
                    _id = ObjectId(),
                    userId = userId,
                    creationDateTime = CustomDateUtils.getUtcDateTime(),
                    value = value.round(2),
                    date = userDate
                )

                val weightEntryResource = userRepository.addWeightEntry(weightEntry = newWeightEntry)

                when (weightEntryResource) {
                    is Resource.Error -> return Resource.Error()
                    is Resource.Success -> {
                        val weightEntries = userRepository.getWeightEntries(
                            userId = userId,
                            limit = 14
                        )

                        return Resource.Success(
                            data = User(
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
        }
        return Resource.Error()
    }
}