package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLongOrZero
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetUserRecipesUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestTimestamp: String?
    ): Resource<List<Recipe>> {
        return diaryRepository.getUserRecipes(
            userId = userId,
            latestTimestamp = latestTimestamp.toLongOrZero()
        )
    }
}