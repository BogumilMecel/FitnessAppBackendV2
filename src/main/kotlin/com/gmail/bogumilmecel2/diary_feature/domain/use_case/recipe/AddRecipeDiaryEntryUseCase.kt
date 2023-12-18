package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class AddRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(request: RecipeDiaryEntryRequest, userId: String): Resource<Boolean> {
        return if (request.servings <= 0 || request.timestamp <= 0 || request.date.isEmpty()) {
            Resource.Error()
        } else {
            val nutritionValues = request.recipe.calculateNutritionValues(request.servings)
            println(nutritionValues.toString())
            println(request.recipe.nutritionValues)
            diaryRepository.insertRecipeDiaryEntry(
                recipeDiaryEntry = RecipeDiaryEntry(
                    id = "",
                    nutritionValues = request.recipe.calculateNutritionValues(request.servings),
                    date = request.date,
                    timestamp = request.timestamp,
                    userId = userId,
                    mealName = request.mealName,
                    portions = request.servings,
                    recipe = request.recipe,
                ),
                userId = userId
            )
        }
    }
}