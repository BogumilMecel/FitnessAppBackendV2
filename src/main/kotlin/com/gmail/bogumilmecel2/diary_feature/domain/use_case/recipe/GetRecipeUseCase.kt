package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.toRecipe
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetRecipeUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(recipeId: String): Resource<Recipe?> {

        return when(val resource = diaryRepository.getRecipe(recipeId)) {
            is Resource.Error -> Resource.Error()
            is Resource.Success -> Resource.Success(data = resource.data?.toRecipe())
        }
    }
}