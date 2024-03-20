package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.Device
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsQuestion
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import org.bson.types.ObjectId

object MockConstants {
    const val EMAIL = "user@email.com"
    const val PASSWORD = "password"
    const val USER_ID_1 = "123456789012345678901111"
    const val USER_ID_2 = "123456789012345678902222"
    const val DEVICE_ID = "123456789012345678903333"
    const val USERNAME = "username"
    private const val DATE = "2021-12-12"
    private const val DATE_TIME = "2023-12-07T00:00:00.000"
    private const val DATE_TIME_ONE_WEEK_LATER = "2023-12-14T00:00:00.000"
    private const val DATE_TIME_TWO_WEEKS_LATER = "2023-12-21T00:00:00.000"
    private const val MOCK_DATE_WITH_PLACEHOLDER = "202%s-12-12"

    fun getDate() = DATE.toLocalDate()
    fun getDateTime() = DATE_TIME.toLocalDateTime()
    fun getDateTimeOneWeekLater() = DATE_TIME_ONE_WEEK_LATER.toLocalDateTime()
    fun getDateTimeTwoWeeksLater() = DATE_TIME_TWO_WEEKS_LATER.toLocalDateTime()
    fun getFormattedDate(value: Int) = MOCK_DATE_WITH_PLACEHOLDER.format(value).toLocalDate()

    fun getUser(askForWeightDaily: Boolean? = null) = UserDto(
        _id = ObjectId(USER_ID_1),
        email = EMAIL,
        username = USERNAME,
        password = PASSWORD,
        salt = "83217893126476",
        logStreak = 1,
        nutritionValues = null,
        userInformation = null,
        askForWeightDaily = askForWeightDaily,
        weightProgress = null,
    )

    fun getDevice() = Device(
        _id = ObjectId(DEVICE_ID),
        userId = USER_ID_1,
        creationDate = DATE_TIME.toLocalDateTime(),
        lastLoggedInDate = DATE_TIME_ONE_WEEK_LATER.toLocalDateTime(),
        deviceId = DEVICE_ID
    )

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
            containerWeight = CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
            nutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
            measurementUnit = MeasurementUnit.GRAMS,
            nutritionValues = getNutritionValues(),
            barcode = BARCODE,
            username = USERNAME,
            userId = USER_ID_1,
            creationDateTime = DATE_TIME.toLocalDateTime(),
            country = Country.POLAND
        )

        fun getRecipe() = RecipeDto(
            _id = RECIPE_ID.toObjectId(),
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
            nutritionValues = getNutritionValues(),
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

        const val WEIGHT_ENTRY_ID_1 = "223456789012345678901111"
        const val WEIGHT_ENTRY_ID_2 = "223456789012345678902222"
        const val VALUE = 80.0

        fun getWeightDialogsQuestions(count: Int = 3) = (1..count).map {
            WeightDialogsQuestion(date = getFormattedDate(it), userId = USER_ID_1)
        }

        fun getWeightEntry(
            id: String = WEIGHT_ENTRY_ID_1,
            value: Double = VALUE,
            creationDateTime: LocalDateTime = DATE_TIME.toLocalDateTime()
        ) = WeightEntryDto(
            _id = ObjectId(id),
            creationDateTime = creationDateTime,
            userId = USER_ID_1,
            date = DATE,
            value = value
        )

        fun getWeightEntries(count: Int) = (1..count).map {
            val number = if (it > 9) "$it" else "0$it"
            getWeightEntry(id = "2234567890123456789011%s".format(number))
        }
    }
}