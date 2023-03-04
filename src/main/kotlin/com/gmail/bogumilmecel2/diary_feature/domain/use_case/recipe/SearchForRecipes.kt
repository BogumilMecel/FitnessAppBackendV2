package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class SearchForRecipes(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(searchText: String): Resource<List<Recipe>>{
        return diaryRepository.searchForRecipes(searchText)
    }
}