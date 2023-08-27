package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryEntriesResponse
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetDiaryEntries(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        date: String,
        userId: String
    ): Resource<DiaryEntriesResponse> {
        val productDiaryEntries = diaryRepository.getProductDiaryEntries(
            date = date,
            userId = userId
        ).data
        val recipeDiaryEntries = diaryRepository.getRecipeDiaryEntries(
            date = date,
            userId = userId
        ).data
        return Resource.Success(
            data = DiaryEntriesResponse(
                recipeDiaryEntries = recipeDiaryEntries ?: emptyList(),
                productDiaryEntries = productDiaryEntries ?: emptyList()
            )
        )
    }

    suspend operator fun invoke(
        userId: String,
        complete: Boolean
    ): Resource<DiaryEntriesResponse>{
        val productDiaryEntries = if (complete) {
            diaryRepository.getProductDiaryEntries(userId = userId).data ?: return Resource.Error()
        } else {
            emptyList()
        }

        val recipeDiaryEntries = if (complete) {
            diaryRepository.getRecipeDiaryEntries(userId = userId).data ?: return Resource.Error()
        } else {
            emptyList()
        }

        return Resource.Success(
            data = DiaryEntriesResponse(
                recipeDiaryEntries = recipeDiaryEntries,
                productDiaryEntries = productDiaryEntries
            )
        )
    }
}