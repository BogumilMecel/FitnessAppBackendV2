package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.UserDiaryItemsResponse
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetUserDiaryItemsUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(userId: String): Resource<UserDiaryItemsResponse> {
        val userProducts = diaryRepository.getUserProducts(userId = userId).data ?: return Resource.Error()
        val userRecipes = diaryRepository.getUserRecipes(userId = userId).data ?: return Resource.Error()

        return Resource.Success(
            data = UserDiaryItemsResponse(
                userProducts = userProducts,
                userRecipes = userRecipes
            )
        )
    }
}