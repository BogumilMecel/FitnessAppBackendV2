package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase

class EditRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeDiaryEntryUseCase: GetRecipeDiaryEntryUseCase,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {
    suspend operator fun invoke(
        recipeDiaryEntry: RecipeDiaryEntry,
        userId: String
    ): Resource<RecipeDiaryEntry> = with(recipeDiaryEntry) {
        val originalRecipeDiaryEntry = getRecipeDiaryEntryUseCase(
            recipeDiaryEntryId = id
        ).data ?: return Resource.Error()

        if (originalRecipeDiaryEntry.userId != userId) return Resource.Error()
        if (servings == originalRecipeDiaryEntry.servings) return Resource.Error()

        val date = originalRecipeDiaryEntry.date ?: return Resource.Error()
        if (!isDateInValidRangeUseCaseUseCase(date)) return Resource.Error()

        val newRecipeDiaryEntry = recipeDiaryEntry.copy(
            changeDateTime = CustomDateUtils.getUtcDateTime()
        )

        val resource = diaryRepository.editRecipeDiaryEntry(
            recipeDiaryEntry = newRecipeDiaryEntry,
            userId = userId,
        )

        return when(resource) {
            is Resource.Error -> {
                Resource.Error()
            }

            is Resource.Success -> {
                Resource.Success(newRecipeDiaryEntry)
            }
        }
    }
}