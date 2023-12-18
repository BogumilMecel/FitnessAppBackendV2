package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.CaloriesSumResponse
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetUserCaloriesSum(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        date:String,
        userId:String
    ):Resource<CaloriesSumResponse>{
        return Resource.Error()
//        return Resource.Success(
//            data = diaryRepository.getUserCaloriesSum(
//                date = date,
//                userId = userId
//            ).data ?: CaloriesSumResponse(caloriesSum = 0)
//        )
    }
}