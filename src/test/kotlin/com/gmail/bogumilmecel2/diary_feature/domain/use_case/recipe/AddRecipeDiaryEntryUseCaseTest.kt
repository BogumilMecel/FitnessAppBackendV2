package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class AddRecipeDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getRecipeUseCase = mockkClass(GetRecipeUseCase::class)
    private val calculateRecipeNutritionValuesUseCase = mockkClass(CalculateRecipeNutritionValuesUseCase::class)
    private val addRecipeDiaryEntryUseCase = AddRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getRecipeUseCase = getRecipeUseCase,
        calculateRecipeNutritionValuesUseCase = calculateRecipeNutritionValuesUseCase
    )

    @Test
    fun `Check if getRecipe returns resource error, resource error is returned`() = runTest {
        mockData(recipeResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if getRecipe returns null, resource error is returned`() = runTest {
        mockData(recipeResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if servings are 0, resource error is returned`() = runTest {
        mockData(recipeResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                recipeDiaryEntryRequest = mockRecipeDiaryEntryRequest().copy(
                    servings = MockConstants.Diary.ZERO_RECIPE_SERVINGS
                )
            )
        )
    }

    @Test
    fun `Check if servings are less than 0, resource error is returned`() = runTest {
        mockData(recipeResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                recipeDiaryEntryRequest = mockRecipeDiaryEntryRequest().copy(
                    servings = MockConstants.Diary.NEGATIVE_RECIPE_SERVINGS
                )
            )
        )
    }

    @Test
    fun `Check if date is empty, resource error is returned`() = runTest {
        mockData(recipeResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                recipeDiaryEntryRequest = mockRecipeDiaryEntryRequest().copy(
                    date = ""
                )
            )
        )
    }

    @Test
    fun `Check if date is invalid, resource error is returned`() = runTest {
        mockData(recipeResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                recipeDiaryEntryRequest = mockRecipeDiaryEntryRequest().copy(
                    date = MockConstants.INVALID_DATE_2021
                )
            )
        )
    }

    @Test
    fun `Check if calculateRecipeNutritionValues returns resource error, resource error is returned`() = runTest {
        mockData(nutritionValuesResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if data is correct and repository returns resource error, resource error is returned`() = runTest {
        mockData(diaryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if data is correct and repository returns resource success, resource success is returned`() = runTest {
        val nutritionValues = MockConstants.Diary.getSampleNutritionValues()
        val request = mockRecipeDiaryEntryRequest()
        mockLocalDate(utcTimestamp = MockConstants.TIMESTAMP)
        mockData()
        assertIs<Resource.Success<Unit>>(callTestedMethod())
        coVerify(exactly = 1) {
            diaryRepository.insertRecipeDiaryEntry(
                recipeDiaryEntry = RecipeDiaryEntry(
                    id = "",
                    nutritionValues = nutritionValues,
                    utcTimestamp = MockConstants.TIMESTAMP,
                    userId = MockConstants.USER_ID,
                    date = request.date,
                    mealName = request.mealName,
                    recipeName = MockConstants.Diary.RECIPE_NAME,
                    recipeId = MockConstants.Diary.RECIPE_ID_31,
                    servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS
                ),
                userId = MockConstants.USER_ID
            )
        }
    }

    private fun mockData(
        recipeResource: Resource<Recipe?> = Resource.Success(mockRecipe()),
        diaryResource: Resource<Unit> = Resource.Success(Unit),
        nutritionValuesResource: Resource<NutritionValues> = Resource.Success(data = MockConstants.Diary.getSampleNutritionValues())
    ) {
        coEvery { getRecipeUseCase(recipeId = MockConstants.Diary.RECIPE_ID_31) } returns recipeResource
        coEvery { diaryRepository.insertRecipeDiaryEntry(recipeDiaryEntry = any(), userId = MockConstants.USER_ID) } returns diaryResource
        coEvery { calculateRecipeNutritionValuesUseCase(recipe = any(), servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS) } returns nutritionValuesResource
    }

    private suspend fun callTestedMethod(recipeDiaryEntryRequest: RecipeDiaryEntryRequest = mockRecipeDiaryEntryRequest()) =
        addRecipeDiaryEntryUseCase(
            request = recipeDiaryEntryRequest,
            userId = MockConstants.USER_ID
        )

    private fun mockRecipe() = Recipe(
        id = MockConstants.Diary.RECIPE_ID_31,
        name = MockConstants.Diary.RECIPE_NAME
    )

    private fun mockRecipeDiaryEntryRequest() = RecipeDiaryEntryRequest(
        date = MockConstants.MOCK_DATE_2021,
        mealName = MealName.BREAKFAST,
        recipeId = MockConstants.Diary.RECIPE_ID_31,
        servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS
    )
}