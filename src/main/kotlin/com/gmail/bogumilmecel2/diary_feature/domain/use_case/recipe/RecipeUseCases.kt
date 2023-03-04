package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

data class RecipeUseCases(
    val insertRecipeUseCase: InsertRecipeUseCase,
    val searchForRecipes: SearchForRecipes,
    val addRecipeDiaryEntryUseCase: AddRecipeDiaryEntryUseCase
)
