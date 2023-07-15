package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class AddRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeUseCase: GetRecipeUseCase
) {

    suspend operator fun invoke(request: RecipeDiaryEntryRequest, userId: String): Resource<Boolean> {
        val recipe = getRecipeUseCase(recipeId = request.recipeId).data ?: return Resource.Error()

        return if (request.servings <= 0 || request.date.isEmpty()) {
            Resource.Error()
        } else if (!request.date.isValidDate()) {
            Resource.Error()
        } else {
            diaryRepository.insertRecipeDiaryEntry(
                recipeDiaryEntry = RecipeDiaryEntry(
                    id = "",
                    nutritionValues = recipe.calculateNutritionValues(request.servings),
                    date = request.date,
                    utcTimestamp = CustomDateUtils.getCurrentUtcTimestamp(),
                    userId = userId,
                    mealName = request.mealName,
                    servings = request.servings,
                    recipeName = recipe.name,
                    recipeId = recipe.id
                ),
                userId = userId
            )
        }
    }
}