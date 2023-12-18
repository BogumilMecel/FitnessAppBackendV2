package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.NewRecipeRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase

class InsertRecipeUseCase(
    private val diaryRepository: DiaryRepository,
    private val isDiaryNameValidUseCase: IsDiaryNameValidUseCase,
    private val getUsernameUseCase: GetUsernameUseCase
) {

    suspend operator fun invoke(
        newRecipeRequest: NewRecipeRequest,
        userId: String,
    ): Resource<Recipe> = with(newRecipeRequest) {
        return if (!isDiaryNameValidUseCase(name = recipeName)) {
            Resource.Error()
        } else if (servings <= 0) {
            Resource.Error()
        } else if (ingredients.size < 2 || ingredients.size > 50) {
            Resource.Error()
        } else if (!checkIfIngredientsAreValid(ingredients = ingredients)) {
            Resource.Error()
        } else {
            getUsernameUseCase(userId = userId)?.let { username ->
                val recipe = Recipe(
                    name = recipeName,
                    ingredients = ingredients,
                    creationDateTime = CustomDateUtils.getUtcDateTime(),
                    nutritionValues = NutritionValues(
                        calories = newRecipeRequest.ingredients.sumOf { it.nutritionValues.calories },
                        carbohydrates = newRecipeRequest.ingredients.sumOf { it.nutritionValues.carbohydrates },
                        protein = newRecipeRequest.ingredients.sumOf { it.nutritionValues.protein },
                        fat = newRecipeRequest.ingredients.sumOf { it.nutritionValues.fat },
                    ),
                    timeRequired = timeRequired,
                    difficulty = difficulty,
                    servings = servings,
                    isPublic = isPublic,
                    username = username,
                    userId = userId,
                    )

                diaryRepository.addNewRecipe(
                    recipe = recipe
                )
            } ?: Resource.Error()
        }
    }

    private fun checkIfIngredientsAreValid(ingredients: List<Ingredient>): Boolean {
        ingredients.forEach { ingredient ->
            if (!isDiaryNameValidUseCase(ingredient.productName)) {
                return false
            }
            if (ingredients.count { it.productId == ingredient.productId } > 1) {
                return false
            }
            if (ingredients.count { it.productName == ingredient.productName } > 1) {
                return false
            }
        }
        return true
    }
}