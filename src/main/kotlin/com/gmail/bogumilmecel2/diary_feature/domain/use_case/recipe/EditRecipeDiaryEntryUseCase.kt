package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
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
        id ?: return Resource.Error(InvalidIdException)
        nutritionValues ?: return Resource.Error(InvalidNutritionValuesException)
        servings ?: return Resource.Error(InvalidServingsException)

        if (servings <= 0) return Resource.Error(InvalidServingsException)

        val originalRecipeDiaryEntry = diaryRepository.getRecipeDiaryEntry(id = id).data ?: return Resource.Error(DiaryEntryNotFoundException)

        if (originalRecipeDiaryEntry.userId != userId) return Resource.Error(ForbiddenException)
        if (servings == originalRecipeDiaryEntry.servings) return Resource.Error(InvalidServingsException)
        if (!isDateInValidRangeUseCaseUseCase(originalRecipeDiaryEntry.creationDateTime.date)) return Resource.Error(InvalidDateException)

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