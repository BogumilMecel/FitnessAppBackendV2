package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import org.apache.commons.lang3.time.DateUtils

object MockConstants {
    const val USER_ID_1 = "user_id_1"
    const val USER_ID_2 = "user_id_2"
    const val USERNAME = "username"
    const val MOCK_DATE_2022 = "2022-12-12"
    const val MOCK_DATE_2021 = "2021-12-12"
    const val INVALID_DATE_2021 = "12-2021-12"
    const val TIMESTAMP = DateUtils.MILLIS_PER_DAY * 30
    const val TIMESTAMP_1_WEEKS_LATER = TIMESTAMP + DateUtils.MILLIS_PER_DAY * 7
    const val TIMESTAMP_MORE_THAN_2_LATER = TIMESTAMP + DateUtils.MILLIS_PER_DAY * 14 + 1

    object Diary {
        private const val PRODUCT_NAME_1 = "Rice"
        const val RECIPE_NAME = "Rice And Chicken"

        const val PRODUCT_ID_11 = "11"
        const val DIARY_ENTRY_ID_21 = "21"
        const val RECIPE_ID_31 = "31"
        const val RECIPE_DIARY_ENTRY_ID_41 = "41"

        const val NEGATIVE_PRODUCT_DIARY_ENTRY_WEIGHT = -1
        const val ZERO_PRODUCT_DIARY_ENTRY_WEIGHT = 0
        const val CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1 = 25
        const val CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2 = 50

        const val CORRECT_RECIPE_SERVINGS_1 = 2
        const val CORRECT_RECIPE_SERVINGS_2 = 3
        const val NEGATIVE_RECIPE_SERVINGS = NEGATIVE_PRODUCT_DIARY_ENTRY_WEIGHT
        const val ZERO_RECIPE_SERVINGS = ZERO_PRODUCT_DIARY_ENTRY_WEIGHT

        fun getSampleNutritionValues() = NutritionValues(calories = 255, carbohydrates = 31.0, protein = 17.0, fat = 7.0)

        fun getSampleNutritionValues2() = NutritionValues(calories = 2863, carbohydrates = 206.4, protein = 152.3, fat = 158.97)

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
            userId = USER_ID_1
        )

        fun getSampleRecipe() = Recipe(
            id = RECIPE_ID_31,
            name = RECIPE_NAME,
            ingredients = emptyList(),
            utcTimestamp = TIMESTAMP,
            imageUrl = null,
            nutritionValues = getSampleNutritionValues(),
            timeRequired = TimeRequired.AVERAGE,
            difficulty = Difficulty.AVERAGE,
            servings = CORRECT_RECIPE_SERVINGS_1,
            isPublic = true,
            username = USERNAME,
            userId = USER_ID_1,
            productPrice = null
        )
    }
}