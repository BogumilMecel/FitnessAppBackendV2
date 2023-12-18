package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class InsertRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val calculateRecipeNutritionValuesUseCase: CalculateRecipeNutritionValuesUseCase
) {

    suspend operator fun invoke(request: RecipeDiaryEntryRequest, userId: String): Resource<RecipeDiaryEntry> {
        val recipe = getRecipeUseCase(recipeId = request.recipeId).data ?: return Resource.Error()

        return if (request.servings <= 0 || request.date.isEmpty()) {
            Resource.Error()
        } else if (!request.date.isValidDate()) {
            Resource.Error()
        } else {
            val currentTimestamp = CustomDateUtils.getCurrentUtcTimestamp()

            diaryRepository.insertRecipeDiaryEntry(
                recipeDiaryEntry = RecipeDiaryEntry(
                    id = "",
                    nutritionValues = calculateRecipeNutritionValuesUseCase(
                        recipe = recipe,
                        servings = request.servings
                    ).data ?: return Resource.Error(),
                    date = request.date,
                    utcTimestamp = currentTimestamp,
                    userId = userId,
                    mealName = request.mealName,
                    servings = request.servings,
                    recipeName = recipe.name,
                    recipeId = recipe.id,
                    lastEditedUtcTimestamp = currentTimestamp
                ),
                userId = userId
            )
        }
    }
}