package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogs
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsLastTimeAsked
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsResponse
import kotlinx.datetime.TimeZone

class HandleWeightDialogsAnswerUseCase(
    private val userRepository: UserRepository,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase
) {
    suspend operator fun invoke(
        userId: String,
        weightDialogsRequest: WeightDialogsRequest,
        timeZone: TimeZone
    ): Resource<WeightDialogsResponse> {
        val userWeightDialogsResource = userRepository.getWeightDialogsData(userId = userId)

        if (userWeightDialogsResource is Resource.Error) return Resource.Error()

        val userWeightDialogsData = userWeightDialogsResource.data

        if (userWeightDialogsData == null) {
            val insertResource = userRepository.insertWeightDialogsData(
                weightDialogs = WeightDialogs(
                    accepted = null,
                    lastTimeAsked = CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone = timeZone).toString(),
                    askedCount = 1
                ),
                userId = userId
            )

            if (insertResource is Resource.Error) return Resource.Error()
        }

        val currentDate = CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString()

        val lastTimeAskedResource = userRepository.updateWeightDialogsLastTimeAsked(
            userId = userId,
            date = currentDate
        )

        if (lastTimeAskedResource is Resource.Error) return Resource.Error()

        if (weightDialogsRequest.accepted != null) {
            val updateResource = userRepository.updateWeightDialogsAccepted(
                userId = userId,
                accepted = weightDialogsRequest.accepted
            )

            if (updateResource is Resource.Error) return Resource.Error()
        }

        return Resource.Success(
            data = WeightDialogsResponse(
                shouldShowWeightPicker = if (weightDialogsRequest.accepted == true) {
                    getWeightEntriesUseCase(
                        userId = userId,
                        limit = 1
                    ).data?.let {
                        it.firstOrNull()?.let { weightEntry ->
                            weightEntry.date != CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString()
                        }
                    } ?: false
                } else false,
                weightDialogsLastTimeAsked = WeightDialogsLastTimeAsked(
                    askedCount = lastTimeAskedResource.data ?: 1,
                    lastTimeAsked = currentDate
                )
            )
        )
    }
}