package com.gmail.bogumilmecel2.user.dates.domain.use_cases

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.copyType
import com.gmail.bogumilmecel2.user.dates.domain.model.AvailableDiaryDatesResponse
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import kotlinx.datetime.TimeZone

class GetAvailableDiaryDatesUseCase(
    private val userRepository: UserRepository,
    private val createAvailableDiaryDatesUseCase: CreateAvailableDiaryDatesUseCase,
) {
    suspend operator fun invoke(
        userId: String,
        timeZone: TimeZone,
    ): Resource<AvailableDiaryDatesResponse> {
        val user = userRepository.getUser(userId = userId).data

        if (user == null) return Resource.Error()

        val availableDatesResource = createAvailableDiaryDatesUseCase(
            availableDaysCount = Constants.Diary.AVAILABLE_DIARY_DATES_COUNT,
            timeZone = timeZone,
        )

        return when (availableDatesResource) {
            is Resource.Error -> availableDatesResource.copyType()
            is Resource.Success -> Resource.Success(
                data = AvailableDiaryDatesResponse(availableDatesResource.data)
            )
        }
    }
}