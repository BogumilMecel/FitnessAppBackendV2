package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDate
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsQuestion
import kotlinx.datetime.toLocalDateTime
import org.bson.types.ObjectId

object MockConstants {
    const val USER_ID_1 = "123456789012345678901111"
    const val USER_ID_2 = "123456789012345678902222"
    const val USERNAME = "username"
    const val DATE = "2021-12-12"
    const val DATE_TIME = "2023-12-07T00:00:00.000"
    const val DATE_TIME_ONE_WEEK_LATER = "2023-12-14T00:00:00.000"
    const val DATE_TIME_TWO_WEEKS_LATER = "2023-12-21T00:00:00.000"
    private const val MOCK_DATE_WITH_PLACEHOLDER = "202%s-12-12"

    fun getFormattedDate(value: Int) = MOCK_DATE_WITH_PLACEHOLDER.format(value).toLocalDate()!!

    object Diary {
        const val PRODUCT_NAME = "Rice"
        const val RECIPE_NAME = "Rice And Chicken"
        const val BARCODE = "1234567890"

        const val PRODUCT_ID = "123456789012345678903333"
        const val PRODUCT_DIARY_ENTRY_ID = "123456789012345678904444"
        const val RECIPE_ID = "123456789012345678905555"
        const val RECIPE_DIARY_ENTRY_ID = "123456789012345678906666"

        const val NEGATIVE_VALUE = -1
        const val ZERO_PRODUCT_DIARY_ENTRY_WEIGHT = 0
        const val CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1 = 25
        const val CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2 = 50

        const val CORRECT_RECIPE_SERVINGS_1 = 2
        const val CORRECT_RECIPE_SERVINGS_2 = 3
        const val ZERO_RECIPE_SERVINGS = ZERO_PRODUCT_DIARY_ENTRY_WEIGHT

        fun getNutritionValues() = NutritionValues(calories = 255, carbohydrates = 31.0, protein = 17.0, fat = 7.0)

        fun getNutritionValues2() = NutritionValues(calories = 2863, carbohydrates = 206.4, protein = 152.3, fat = 158.97)

        fun getProduct() = ProductDto(
            _id = ObjectId(PRODUCT_ID),
            name = PRODUCT_NAME,
            containerWeight = null,
            nutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
            measurementUnit = MeasurementUnit.GRAMS,
            nutritionValues = getNutritionValues(),
            barcode = null,
            username = "abc",
            userId = USER_ID_1,
            creationDateTime = DATE_TIME.toLocalDateTime(),
            country = Country.POLAND
        )

        fun getRecipe() = Recipe(
            id = RECIPE_ID,
            name = RECIPE_NAME,
            ingredients = emptyList(),
            imageUrl = null,
            nutritionValues = getNutritionValues(),
            timeRequired = TimeRequired.AVERAGE,
            difficulty = Difficulty.AVERAGE,
            servings = CORRECT_RECIPE_SERVINGS_1,
            isPublic = true,
            username = USERNAME,
            userId = USER_ID_1,
            creationDateTime = DATE_TIME.toLocalDateTime(),
        )

        fun getProductDiaryEntry() = ProductDiaryEntryDto(
            _id = PRODUCT_DIARY_ENTRY_ID.toObjectId(),
            nutritionValues = getNutritionValues(),
            date = DATE,
            mealName = MealName.BREAKFAST,
            userId = USER_ID_1,
            measurementUnit = MeasurementUnit.GRAMS,
            productName = PRODUCT_NAME,
            productId = PRODUCT_ID,
            weight = CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
            creationDateTime = DATE_TIME.toLocalDateTime(),
            changeDateTime = DATE_TIME_ONE_WEEK_LATER.toLocalDateTime(),
            deleted = false
        )

        fun getRecipeDiaryEntry() = RecipeDiaryEntryDto(
            _id = RECIPE_DIARY_ENTRY_ID.toObjectId(),
            nutritionValues = getNutritionValues2(),
            recipeName = RECIPE_NAME,
            recipeId = RECIPE_ID,
            servings = CORRECT_RECIPE_SERVINGS_1,
            userId = USER_ID_1,
            date = DATE,
            mealName = MealName.BREAKFAST,
            creationDateTime = DATE_TIME.toLocalDateTime(),
            changeDateTime = DATE_TIME_ONE_WEEK_LATER.toLocalDateTime(),
            deleted = false
        )
    }

    object Weight {
        fun getWeightDialogsQuestions(count: Int = 3) = (1..count).map {
            WeightDialogsQuestion(date = getFormattedDate(it), userId = USER_ID_1)
        }
    }
}