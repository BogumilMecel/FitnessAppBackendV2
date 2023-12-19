package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import org.bson.types.ObjectId

class InsertRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {

    suspend operator fun invoke(request: RecipeDiaryEntryRequest, userId: String): Resource<RecipeDiaryEntry> = with(request) {
        val recipe = getRecipeUseCase(recipeId = request.recipeId).data ?: return Resource.Error()

        if (servings <= 0) return Resource.Error()
        if (!isDateInValidRangeUseCaseUseCase(date)) return Resource.Error()

        val currentDateTime = CustomDateUtils.getUtcDateTime()

        return diaryRepository.insertRecipeDiaryEntry(
            recipeDiaryEntry = RecipeDiaryEntryDto(
                _id = ObjectId(),
                nutritionValues = nutritionValues,
                date = date.toString(),
                creationDateTime = currentDateTime,
                userId = userId,
                mealName = mealName,
                servings = servings,
                recipeName = recipe.name ?: return Resource.Error(),
                recipeId = recipe.id ?: return Resource.Error(),
                changeDateTime = currentDateTime,
                deleted = false
            ),
            userId = userId
        )
    }
}