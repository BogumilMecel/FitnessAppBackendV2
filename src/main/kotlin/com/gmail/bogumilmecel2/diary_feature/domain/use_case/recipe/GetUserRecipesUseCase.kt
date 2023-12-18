package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.CustomDateUtils.getOrMinDateTime
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import kotlinx.datetime.LocalDateTime

class GetUserRecipesUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestDateTime: LocalDateTime?
    ): Resource<List<Recipe>> {
        return diaryRepository.getUserRecipes(
            userId = userId,
            latestDateTime = latestDateTime.getOrMinDateTime()
        )
    }
}