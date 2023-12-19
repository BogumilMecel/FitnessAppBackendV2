package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryEntriesResponse
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import kotlinx.datetime.LocalDate

class GetDiaryEntries(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        date: LocalDate,
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
}