package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(recipeDiaryEntryId: String): Resource<RecipeDiaryEntry> {
        if (recipeDiaryEntryId.isEmpty()) {
            return Resource.Error()
        }

        val recipeDiaryEntry = diaryRepository.getRecipeDiaryEntry(
            id = recipeDiaryEntryId
        ).data ?: return Resource.Error()

        return Resource.Success(
            data = recipeDiaryEntry
        )
    }
}