package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductDiaryHistoryUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestEntryTimestamp: String?
    ): Resource<List<ProductDiaryHistoryItem>> {
        val latestEntryTimestampValue = latestEntryTimestamp?.toLongOrNull() ?: 0

        return diaryRepository.getProductDiaryHistory(
            userId = userId,
            fromTimestamp = latestEntryTimestampValue
        )
    }
}