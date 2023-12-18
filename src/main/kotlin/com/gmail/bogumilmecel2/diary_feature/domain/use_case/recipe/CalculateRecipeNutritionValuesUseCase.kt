package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe

class CalculateRecipeNutritionValuesUseCase {
    operator fun invoke(
        recipe: Recipe,
        servings: Int
    ): Resource<NutritionValues> {
        if (servings <= 0) return Resource.Error()

        with(recipe.nutritionValues) {
            return Resource.Success(
                data = NutritionValues(
                    calories = (calories.toDouble() * (servings.toDouble() / recipe.servings.toDouble())).toInt(),
                    carbohydrates = (carbohydrates * (servings.toDouble() / recipe.servings.toDouble())).round(2),
                    protein = (protein * (servings.toDouble() / recipe.servings.toDouble())).round(2),
                    fat = (fat * (servings.toDouble() / recipe.servings.toDouble())).round(2)
                )
            )
        }
    }
}
