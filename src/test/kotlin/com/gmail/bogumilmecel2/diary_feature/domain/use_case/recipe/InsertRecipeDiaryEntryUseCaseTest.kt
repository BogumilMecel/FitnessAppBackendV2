package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDate
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDateTime
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class InsertRecipeDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getRecipeUseCase = mockkClass(GetRecipeUseCase::class)
    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val insertRecipeDiaryEntryUseCase = InsertRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getRecipeUseCase = getRecipeUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
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
        mockData()
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
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                recipeDiaryEntryRequest = mockRecipeDiaryEntryRequest().copy(
                    servings = MockConstants.Diary.NEGATIVE_VALUE
                )
            )
        )
    }

    @Test
    fun `Check if date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if data is correct and repository returns resource error, resource error is returned`() = runTest {
        mockData()
        mockRepositoryResponse()
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if data is correct and repository returns resource success, resource success is returned`() = runTest {
        val request = mockRecipeDiaryEntryRequest()
        mockDateTime(dateTime = MockConstants.DATE_TIME.toLocalDateTime()!!)
        mockData()
        val expectedRecipeDiaryEntry = RecipeDiaryEntry(
            id = "",
            nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
            creationDateTime = MockConstants.DATE_TIME.toLocalDateTime(),
            userId = MockConstants.USER_ID_1,
            date = request.date,
            mealName = request.mealName,
            recipeName = MockConstants.Diary.RECIPE_NAME,
            recipeId = MockConstants.Diary.RECIPE_ID_31,
            servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1,
            changeDateTime = MockConstants.DATE_TIME.toLocalDateTime()
        )
        mockRepositoryResponse(Resource.Success(expectedRecipeDiaryEntry))
        assertIs<Resource.Success<Unit>>(callTestedMethod())
        coVerify(exactly = 1) {
            diaryRepository.insertRecipeDiaryEntry(
                recipeDiaryEntry = expectedRecipeDiaryEntry,
                userId = MockConstants.USER_ID_1
            )
        }
    }

    private fun mockData(
        recipeResource: Resource<Recipe?> = Resource.Success(MockConstants.Diary.getSampleRecipe()),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { getRecipeUseCase(recipeId = MockConstants.Diary.RECIPE_ID_31) } returns recipeResource
        every { isDateInValidRangeUseCaseUseCase(date = MockConstants.DATE.toLocalDate()!!) } returns isDateInValidRange
    }

    private fun mockRepositoryResponse(diaryResource: Resource<RecipeDiaryEntry> = Resource.Error()) {
        coEvery { diaryRepository.insertRecipeDiaryEntry(recipeDiaryEntry = any(), userId = MockConstants.USER_ID_1) } returns diaryResource
    }

    private suspend fun callTestedMethod(recipeDiaryEntryRequest: RecipeDiaryEntryRequest = mockRecipeDiaryEntryRequest()) =
        insertRecipeDiaryEntryUseCase(
            request = recipeDiaryEntryRequest,
            userId = MockConstants.USER_ID_1
        )

    private fun mockRecipeDiaryEntryRequest() = RecipeDiaryEntryRequest(
        date = MockConstants.DATE.toLocalDate()!!,
        mealName = MealName.BREAKFAST,
        recipeId = MockConstants.Diary.RECIPE_ID_31,
        servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1,
        nutritionValues = MockConstants.Diary.getSampleNutritionValues()
    )
}