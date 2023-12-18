package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import kotlinx.datetime.LocalDate

class InsertRecipeDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getRecipeUseCase: GetRecipeUseCase,
) {

    suspend operator fun invoke(request: RecipeDiaryEntryRequest, userId: String): Resource<RecipeDiaryEntry> = with(request) {
        val recipe = getRecipeUseCase(recipeId = request.recipeId).data ?: return Resource.Error()

        if (servings <= 0) return Resource.Error()
        if (date.isEmpty()) return Resource.Error()
        if (!date.isValidDate()) return Resource.Error()
        if (CustomDateUtils.getDaysFromNow(from = LocalDate.parse(date)) > Constants.Diary.MAXIMUM_MODIFY_DATE) return Resource.Error()

        val currentTimestamp = CustomDateUtils.getCurrentUtcTimestamp()

        return diaryRepository.insertRecipeDiaryEntry(
            recipeDiaryEntry = RecipeDiaryEntry(
                id = "",
                nutritionValues = nutritionValues,
                date = date,
                utcTimestamp = currentTimestamp,
                userId = userId,
                mealName = mealName,
                servings = servings,
                recipeName = recipe.name,
                recipeId = recipe.id,
                editedUtcTimestamp = currentTimestamp
            ),
            userId = userId
        )
    }
}