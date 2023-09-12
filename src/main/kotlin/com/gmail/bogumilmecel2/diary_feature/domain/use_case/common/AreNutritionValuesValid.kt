package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.util.extensions.isNumberNatural
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues

class AreNutritionValuesValid {
    operator fun invoke(nutritionValues: NutritionValues): Boolean = with(nutritionValues) {
        return calories.isNumberNatural() && carbohydrates.isNumberNatural() && protein.isNumberNatural() && fat.isNumberNatural()
    }
}