package com.gmail.bogumilmecel2.user.dates.domain.use_cases

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.dates.domain.model.AvailableDiaryDatesResponse
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class GetAvailableDiaryDatesUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        userId: String,
    ): Resource<AvailableDiaryDatesResponse> {
        val user = userRepository.getUser(userId = userId).data

        if (user == null) return Resource.Error()

        return Resource.Success(AvailableDiaryDatesResponse(Constants.Diary.AVAILABLE_DIARY_DATES_COUNT))
    }
}