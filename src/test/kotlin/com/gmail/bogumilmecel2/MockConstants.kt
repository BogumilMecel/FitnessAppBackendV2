package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product

object MockConstants {
    const val USER_ID = "user_id"
    const val MOCK_DATE_2022 = "2022-12-12"
    const val MOCK_DATE_2021 = "2021-12-12"

    object Diary {
        private const val PRODUCT_NAME_1 = "Rice"
        const val PRODUCT_ID_11 = "11"
        const val DIARY_ENTRY_ID_21 = "21"
        const val NEGATIVE_PRODUCT_DIARY_ENTRY_WEIGHT = -1
        const val ZERO_PRODUCT_DIARY_ENTRY_WEIGHT = 0
        const val CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1 = 25
        const val CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2 = 50

        fun getSampleNutritionValues() = NutritionValues(calories = 255, carbohydrates = 31.0, protein = 17.0, fat = 7.0)
        fun getSampleProduct() = Product(
            id = PRODUCT_ID_11,
            name = PRODUCT_NAME_1,
            containerWeight = null,
            utcTimestamp = 1,
            nutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
            measurementUnit = MeasurementUnit.GRAMS,
            nutritionValues = getSampleNutritionValues(),
            barcode = null,
            username = "abc",
            userId = USER_ID
        )
    }
}