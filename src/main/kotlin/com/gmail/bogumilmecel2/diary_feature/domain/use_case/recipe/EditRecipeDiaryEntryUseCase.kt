package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.toRecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase

class EditRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {
    suspend operator fun invoke(
        recipeDiaryEntry: RecipeDiaryEntry,
        userId: String
    ): Resource<RecipeDiaryEntry> = with(recipeDiaryEntry) {
        id ?: return Resource.Error()
        nutritionValues ?: return Resource.Error()
        servings ?: return Resource.Error()

        val originalRecipeDiaryEntry = diaryRepository.getRecipeDiaryEntry(id = id).data ?: return Resource.Error()

        if (originalRecipeDiaryEntry.userId != userId) return Resource.Error()
        if (servings == originalRecipeDiaryEntry.servings) return Resource.Error()
        if (!isDateInValidRangeUseCaseUseCase(originalRecipeDiaryEntry.creationDateTime.date)) return Resource.Error()

        val newRecipeDiaryEntry = originalRecipeDiaryEntry.copy(
            changeDateTime = CustomDateUtils.getUtcDateTime(),
            nutritionValues = nutritionValues,
            servings = servings,
        )

        return when(diaryRepository.editRecipeDiaryEntry(recipeDiaryEntry = newRecipeDiaryEntry)) {
            is Resource.Error -> Resource.Error()
            is Resource.Success -> Resource.Success(newRecipeDiaryEntry.toRecipeDiaryEntry())
        }
    }
}