package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import org.bson.types.ObjectId

class InsertRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {

    suspend operator fun invoke(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<RecipeDiaryEntry> = with(recipeDiaryEntry) {
        val recipe = diaryRepository.getRecipe(recipeId).data ?: return Resource.Error(RecipeNotFoundException)

        if (servings <= 0) return Resource.Error(InvalidServingsException)
        if (!isDateInValidRangeUseCaseUseCase(date)) return Resource.Error(DateNotInRangeException)

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
                recipeName = recipe.name,
                recipeId = recipe._id.toString(),
                changeDateTime = currentDateTime,
                deleted = false
            )
        )
    }
}