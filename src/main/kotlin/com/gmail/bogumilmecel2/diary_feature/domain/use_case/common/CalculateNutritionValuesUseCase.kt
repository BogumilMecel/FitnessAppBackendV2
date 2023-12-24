package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.domain.model.exceptions.InvalidWeightException
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues

class CalculateNutritionValuesUseCase {
    operator fun invoke(
        nutritionValues: NutritionValues,
        weight: Int
    ): Resource<NutritionValues> = with(nutritionValues) {
        if (weight <= 0) return Resource.Error(InvalidWeightException)

        val doubleWeight = weight.toDouble()

        return Resource.Success(
            data = NutritionValues(
                calories = ((calories).toDouble() / 100.0 * doubleWeight).toInt(),
                carbohydrates = (carbohydrates / 100.0 * doubleWeight).round(2),
                protein = (protein / 100.0 * doubleWeight).round(2),
                fat = (fat / 100.0 * doubleWeight).round(2),
            )
        )
    }
}