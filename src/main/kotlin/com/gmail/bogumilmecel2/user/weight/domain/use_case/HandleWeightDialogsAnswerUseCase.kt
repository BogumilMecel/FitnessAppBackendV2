package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogs
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import kotlinx.datetime.TimeZone

class HandleWeightDialogsAnswerUseCase(
    private val userRepository: UserRepository,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase
) {
    suspend operator fun invoke(
        userId: String,
        weightDialogsRequest: WeightDialogsRequest,
        timeZone: TimeZone
    ): Resource<WeightDialogs> {
        val user = userRepository.getUser(userId = userId).data ?: return Resource.Error()

        val currentDate = CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString()

        val userWeightDialogsData = user.weightDialogs

        if (userWeightDialogsData != null && userWeightDialogsData.lastTimeAsked == currentDate) return Resource.Error()

        val askedCount = userWeightDialogsData?.askedCount?.plus(1) ?: 1

        val updateResource = userRepository.updateWeightDialogsData(
            weightDialogs = WeightDialogs(
                accepted = weightDialogsRequest.accepted,
                lastTimeAsked = currentDate,
                askedCount = askedCount
            ),
            userId = userId
        )

        if (updateResource is Resource.Error) return Resource.Error()

        return Resource.Success(
            data = WeightDialogs(
                askedCount = askedCount,
                lastTimeAsked = currentDate,
                accepted = weightDialogsRequest.accepted
            )
        )
    }
}