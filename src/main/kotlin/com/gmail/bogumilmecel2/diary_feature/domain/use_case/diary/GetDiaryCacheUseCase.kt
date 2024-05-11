package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryCacheRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryCacheResponse
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class GetDiaryCacheUseCase(
    private val diaryRepository: DiaryRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        diaryCacheRequest: DiaryCacheRequest
    ): Resource<DiaryCacheResponse> {
        if (userRepository.getUser(userId = userId).data == null) return Resource.Error()

        return Resource.Success(
            data = DiaryCacheResponse(
                products = diaryRepository.getUserProducts(
                    userId = userId,
                    latestDateTime = diaryCacheRequest.latestProductDateTime
                ).data ?: return Resource.Error(),

                recipes = diaryRepository.getUserRecipes(
                    userId = userId,
                    latestDateTime = diaryCacheRequest.latestRecipeDateTime
                ).data ?: return Resource.Error(),

                productDiaryEntries = diaryRepository.getProductDiaryEntries(
                    userId = userId,
                    latestDateTime = diaryCacheRequest.latestProductDiaryEntryDateTime
                ).data ?: return Resource.Error(),

                recipeDiaryEntries = diaryRepository.getRecipeDiaryEntries(
                    userId = userId,
                    latestDateTime = diaryCacheRequest.latestRecipeDiaryEntryDateTime
                ).data ?: return Resource.Error(),
            )
        )
    }
}