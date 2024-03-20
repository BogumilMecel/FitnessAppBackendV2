package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.toRecipe
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertIs

class InsertRecipeDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val insertRecipeDiaryEntryUseCase = InsertRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if recipe id is null resource error is returned`() = runTest {
        callTestedMethod(recipeId = null).assertIsError(InvalidIdException)
    }

    @Test
    fun `Check if servings are null resource error is returned`() = runTest {
        callTestedMethod(servings = null).assertIsError(InvalidServingsException)
    }

    @Test
    fun `Check if nutrition values are null resource error is returned`() = runTest {
        callTestedMethod(nutritionValues = null).assertIsError(InvalidNutritionValuesException)
    }

    @Test
    fun `Check if date is null resource error is returned`() = runTest {
        callTestedMethod(date = null).assertIsError(InvalidDateException)
    }

    @Test
    fun `Check if meal name is null resource error is returned`() = runTest {
        callTestedMethod(mealName = null).assertIsError(InvalidMealNameException)
    }

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
        callTestedMethod(servings = MockConstants.Diary.ZERO_RECIPE_SERVINGS).assertIsError(InvalidServingsException)
    }

    @Test
    fun `Check if servings are less than 0, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(servings = MockConstants.Diary.NEGATIVE_VALUE).assertIsError(InvalidServingsException)
    }

    @Test
    fun `Check if date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if data is correct and repository returns resource error, resource error is returned`() = runTest {
        mockData(repositoryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if data is correct and repository returns resource success, resource success is returned`() = runTest {
        mockDateTime(dateTime = MockConstants.getDateTime())
        mockData()
        callTestedMethod().assertIsSuccess()
        coVerify(exactly = 1) { diaryRepository.insertRecipeDiaryEntry(recipeDiaryEntry = any()) }
    }

    private fun mockData(
        recipeResource: Resource<RecipeDto?> = Resource.Success(MockConstants.Diary.getRecipe()),
        repositoryResource: Resource<RecipeDiaryEntry> = Resource.Success(MockConstants.Diary.getRecipeDiaryEntry().toRecipe()),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { diaryRepository.getRecipe(recipeId = MockConstants.Diary.RECIPE_ID) } returns recipeResource
        coEvery { diaryRepository.insertRecipeDiaryEntry(recipeDiaryEntry = any()) } returns repositoryResource
        every { isDateInValidRangeUseCaseUseCase(date = MockConstants.getDate()) } returns isDateInValidRange
    }

    private suspend fun callTestedMethod(
        recipeId: String? = MockConstants.Diary.RECIPE_ID,
        servings: Int? = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1,
        date: LocalDate? = MockConstants.getDate(),
        nutritionValues: NutritionValues? = MockConstants.Diary.getNutritionValues(),
        mealName: MealName? = MealName.BREAKFAST
    ) = insertRecipeDiaryEntryUseCase(
        recipeDiaryEntry = MockConstants.Diary.getRecipeDiaryEntry().toRecipe().copy(
            recipeId = recipeId,
            servings = servings,
            date = date,
            nutritionValues = nutritionValues,
            mealName = mealName
        ),
        userId = MockConstants.USER_ID_1
    )
}