package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.EditRecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class EditRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeDiaryEntryUseCase: GetRecipeDiaryEntryUseCase
) {
    suspend operator fun invoke(
        editRecipeDiaryEntryRequest: EditRecipeDiaryEntryRequest,
        userId: String
    ): Resource<Unit> = with(editRecipeDiaryEntryRequest) {
        if (newServings <= 0) {
            return Resource.Error()
        }

        val recipeDiaryEntry = getRecipeDiaryEntryUseCase(recipeDiaryEntryId = recipeDiaryEntryId).data ?: return Resource.Error()

        if (recipeDiaryEntry.userId != userId) {
            return Resource.Error()
        }

        if (recipeDiaryEntry.servings == newServings) {
            return Resource.Error()
        }

        val newNutritionValues = recipeDiaryEntry.recipe.calculateNutritionValues(servings = newServings)

        val wasAcknowledged = diaryRepository.editRecipeDiaryEntry(
            recipeDiaryEntry = recipeDiaryEntry.copy(
                servings = newServings,
                nutritionValues = newNutritionValues
            ),
            userId = userId
        ).data ?: return Resource.Error()

        if (!wasAcknowledged) {
            return Resource.Error()
        }

        return Resource.Success(Unit)
    }
}