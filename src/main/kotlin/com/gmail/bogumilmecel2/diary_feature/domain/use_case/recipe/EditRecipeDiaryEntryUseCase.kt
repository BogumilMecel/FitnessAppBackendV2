package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.EditRecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class EditRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeDiaryEntryUseCase: GetRecipeDiaryEntryUseCase,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val calculateRecipeNutritionValuesUseCase: CalculateRecipeNutritionValuesUseCase
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

        val recipe = getRecipeUseCase(recipeId = recipeDiaryEntry.recipeId).data ?: return Resource.Error()

        val newNutritionValues = calculateRecipeNutritionValuesUseCase(
            recipe = recipe,
            servings = newServings
        ).data ?: return Resource.Error()

        return diaryRepository.editRecipeDiaryEntry(
            recipeDiaryEntry = recipeDiaryEntry.copy(
                servings = newServings,
                nutritionValues = newNutritionValues
            ),
            userId = userId
        )
    }
}