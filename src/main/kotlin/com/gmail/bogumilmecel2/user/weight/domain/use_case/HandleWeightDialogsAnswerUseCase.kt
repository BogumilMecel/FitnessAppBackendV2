package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUserObjectUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsAnswer
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest

class HandleWeightDialogsAnswerUseCase(
    private val userRepository: UserRepository,
    private val getUserObjectUseCase: GetUserObjectUseCase
) {
    suspend operator fun invoke(
        userId: String,
        weightDialogsRequest: WeightDialogsRequest
    ): Resource<Unit> {
        val user = getUserObjectUseCase(userId = userId).data ?: return Resource.Error()

        if (user.weightDialogsAccepted) return Resource.Error()

        when(weightDialogsRequest.answer) {
            WeightDialogsAnswer.NotNow -> {

            }
        }
    }
}