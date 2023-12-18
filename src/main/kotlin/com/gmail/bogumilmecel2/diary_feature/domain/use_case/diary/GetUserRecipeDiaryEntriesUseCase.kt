package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLongOrZero
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetUserRecipeDiaryEntriesUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestTimestamp: String?
    ): Resource<List<RecipeDiaryEntry>> {
        return diaryRepository.getRecipeDiaryEntries(
            userId = userId,
            latestTimestamp = latestTimestamp.toLongOrZero()
        )
    }
}