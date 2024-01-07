package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.domain.model.exceptions.DateNotInRangeException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.InvalidDateException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.InvalidWeightValueException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UserNotFoundException
import com.gmail.bogumilmecel2.common.domain.use_case.CheckIfUserExistsUseCase
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.CheckIfWeightIsValidUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import org.bson.types.ObjectId

class AddWeightEntryUseCase(
    private val userRepository: UserRepository,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val checkIfWeightIsValidUseCase: CheckIfWeightIsValidUseCase,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase,
    private val checkIfUserExistsUseCase: CheckIfUserExistsUseCase
) {

    suspend operator fun invoke(
        weightEntry: WeightEntry,
        userId: String
    ): Resource<User> = with(weightEntry) {
        value ?: return Resource.Error(InvalidWeightValueException)
        date ?: return Resource.Error(InvalidDateException)

        if (!checkIfUserExistsUseCase(userId = userId)) return Resource.Error(UserNotFoundException)
        if (!checkIfWeightIsValidUseCase(value)) return Resource.Error(InvalidWeightValueException)
        if (!isDateInValidRangeUseCaseUseCase(date = date)) return Resource.Error(DateNotInRangeException)

        val removeResource = userRepository.removeWeightEntries(userId = userId, date = date)
        if (removeResource is Resource.Error) return Resource.Error()

        val weightEntryResource = userRepository.addWeightEntry(
            weightEntry = WeightEntryDto(
                _id = ObjectId(),
                userId = userId,
                creationDateTime = CustomDateUtils.getUtcDateTime(),
                value = value.round(2),
                date = date.toString()
            )
        )

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