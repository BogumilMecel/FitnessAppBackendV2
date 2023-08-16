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
        latestProductDiaryEntryUtcTimestampString: String?,
        latestRecipeDiaryEntryUtcTimestampString: String?
    ): Resource<DiaryEntriesResponse> {
        val productDiaryEntries = if (latestProductDiaryEntryUtcTimestampString == null) {
            diaryRepository.getProductDiaryEntries(userId = userId)
        } else {
            diaryRepository.getProductDiaryEntries(
                userId = userId,
                latestProductDiaryEntryTimestamp = latestProductDiaryEntryUtcTimestampString.toLongOrNull() ?: return Resource.Error()
            )
        }.data ?: return Resource.Error()

        val recipeDiaryEntries = if (latestRecipeDiaryEntryUtcTimestampString == null) {
            diaryRepository.getRecipeDiaryEntries(userId = userId)
        } else {
            diaryRepository.getRecipeDiaryEntries(
                userId = userId,
                latestRecipeDiaryEntryTimestamp = latestRecipeDiaryEntryUtcTimestampString.toLongOrNull() ?: return Resource.Error()
            )
        }.data ?: return Resource.Error()

        return Resource.Success(
            data = DiaryEntriesResponse(
                productDiaryEntries = productDiaryEntries,
                recipeDiaryEntries = recipeDiaryEntries
            )
        )
    }
}