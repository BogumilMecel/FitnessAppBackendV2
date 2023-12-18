package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUserObjectUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsResponse
import kotlinx.datetime.TimeZone

class HandleWeightDialogsAnswerUseCase(
    private val userRepository: UserRepository,
    private val getUserObjectUseCase: GetUserObjectUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase
) {
    suspend operator fun invoke(
        userId: String,
        weightDialogsRequest: WeightDialogsRequest,
        timezone: TimeZone
    ): Resource<WeightDialogsResponse> {
        val user = getUserObjectUseCase(userId = userId).data ?: return Resource.Error()

        if (user.weightDialogsAccepted == true) return Resource.Error()

        val lastTimeAskedResource =

        when(weightDialogsRequest.accepted) {
            true -> {
                val resource = userRepository.updateWeightDialogsQuestion(
                    accepted = true,
                    userId = userId
                )

                when(resource) {
                    is Resource.Success -> {
                        val latestWeightEntry = getWeightEntriesUseCase(
                            userId = userId,
                            limit = 1
                        ).data

                        return Resource.Success(
                            data = WeightDialogsResponse(
                                shouldShowWeightPicker = latestWeightEntry?.firstOrNull()?.date == CustomDateUtils.getCurrentTimeZoneLocalDateString(timezone),
                                weightDialogsAccepted = true,
                                weightDialogsLastTimeAsked = null
                            )
                        )
                    }

                    is Resource.Error -> {
                        return Resource.Error()
                    }
                }
            }

            false -> {
                return Resource.Error()
            }

            null -> {
                return Resource.Error()
            }
        }
    }
}