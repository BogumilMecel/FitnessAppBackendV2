package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues

class CalculateRecipeNutritionValuesUseCase {
    operator fun invoke(
        nutritionValues: NutritionValues,
        recipeServings: Int,
        servings: Int
    ): Resource<NutritionValues> {
        if (servings <= 0) return Resource.Error()
        if (recipeServings <= 0) return Resource.Error()

        val doubleRecipeServings = recipeServings.toDouble()

        with(nutritionValues) {
            return Resource.Success(
                data = NutritionValues(
                    calories = (calories.toDouble() * (servings.toDouble() / doubleRecipeServings)).toInt(),
                    carbohydrates = (carbohydrates * (servings.toDouble() / doubleRecipeServings)).round(2),
                    protein = (protein * (servings.toDouble() / doubleRecipeServings)).round(2),
                    fat = (fat * (servings.toDouble() / doubleRecipeServings)).round(2)
                )
            )
        }
    }
}
