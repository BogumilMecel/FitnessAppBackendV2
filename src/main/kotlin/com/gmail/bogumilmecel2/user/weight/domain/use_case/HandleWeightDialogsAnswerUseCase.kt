package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsQuestion
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import kotlinx.datetime.TimeZone

class HandleWeightDialogsAnswerUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        userId: String,
        weightDialogsRequest: WeightDialogsRequest,
        timeZone: TimeZone
    ): Resource<Unit> = with(weightDialogsRequest) {
        val currentDate = CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString()
        val weightDialogsQuestions = userRepository.getWeightDialogsQuestions(userId = userId).data ?: return Resource.Error()

        if (accepted == null) {
            return if (weightDialogsQuestions.size < 4) {
                userRepository.insertWeightDialogsQuestion(
                    weightDialogsQuestion = WeightDialogsQuestion(
                        date = currentDate,
                        userId = userId
                    ),
                )
            } else {
                Resource.Error()
            }
        }

        return userRepository.updateAskForWeightDaily(
            accepted = accepted,
            userId = userId
        )
    }
}