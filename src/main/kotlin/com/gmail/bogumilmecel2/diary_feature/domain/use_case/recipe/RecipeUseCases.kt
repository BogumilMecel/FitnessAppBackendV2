package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetRecipePriceUseCase

data class RecipeUseCases(
    val insertRecipeUseCase: InsertRecipeUseCase,
    val searchForRecipes: SearchForRecipes,
    val addRecipeDiaryEntryUseCase: AddRecipeDiaryEntryUseCase,
    val getRecipePriceUseCase: GetRecipePriceUseCase,
    val getRecipeUseCase: GetRecipeUseCase
)
