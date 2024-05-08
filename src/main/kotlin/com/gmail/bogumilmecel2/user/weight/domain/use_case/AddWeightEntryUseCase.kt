package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.toUser
import com.gmail.bogumilmecel2.common.domain.model.exceptions.DateNotInRangeException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.InvalidWeightValueException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UserNotFoundException
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.CheckIfWeightIsValidUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import com.gmail.bogumilmecel2.user.weight.domain.model.toWeightEntry
import kotlinx.datetime.LocalDate
import org.bson.types.ObjectId

class AddWeightEntryUseCase(
    private val userRepository: UserRepository,
    private val calculateWeightProgressUseCase: CalculateWeightProgressUseCase,
    private val checkIfWeightIsValidUseCase: CheckIfWeightIsValidUseCase,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase,
) {

    suspend operator fun invoke(
        value: Double,
        date: LocalDate,
        userId: String
    ): Resource<User> {
        val user = userRepository.getUser(userId = userId).data ?: return Resource.Error(UserNotFoundException)
        if (!checkIfWeightIsValidUseCase(value)) return Resource.Error(InvalidWeightValueException)
        if (!isDateInValidRangeUseCaseUseCase(date = date)) return Resource.Error(DateNotInRangeException)

        val removeResource = userRepository.removeWeightEntries(userId = userId, date = date)
        if (removeResource is Resource.Error) return Resource.Error()

        val weightEntry = WeightEntryDto(
            _id = ObjectId(),
            userId = userId,
            creationDateTime = CustomDateUtils.getUtcDateTime(),
            value = value.round(2),
            date = date.toString()
        )

        val weightEntryResource = userRepository.addWeightEntry(weightEntry = weightEntry)

        when (weightEntryResource) {
            is Resource.Error -> return Resource.Error()
            is Resource.Success -> {
                val weightEntries = userRepository.getWeightEntries(
                    userId = userId,
                    limit = 14
                )

                return Resource.Success(
                    data = user.toUser(latestWeightEntry = weightEntry.toWeightEntry()).copy(
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