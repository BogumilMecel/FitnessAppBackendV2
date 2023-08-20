package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.EditRecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class EditRecipeDiaryEntryUseCaseTest: BaseDiaryTest() {

    private val calculateRecipeNutritionValuesUseCase = mockkClass(CalculateRecipeNutritionValuesUseCase::class)
    private val getRecipeUseCase = mockkClass(GetRecipeUseCase::class)
    private val getRecipeDiaryEntryUseCase = mockkClass(GetRecipeDiaryEntryUseCase::class)
    private val editRecipeDiaryEntryUseCase = EditRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        calculateRecipeNutritionValuesUseCase = calculateRecipeNutritionValuesUseCase,
        getRecipeUseCase = getRecipeUseCase,
        getRecipeDiaryEntryUseCase = getRecipeDiaryEntryUseCase
    )

    @Test
    fun `Check if getRecipeDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if getRecipeDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if user id do not match, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                userId = MockConstants.USER_ID_2
            )
        )
    }

    @Test
    fun `Check if request servings are the same as entry servings, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                editRecipeDiaryEntryRequest = mockEditRecipeDiaryEntryRequest().copy(newServings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2  )
            )
        )
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
    fun `Check if calculateRecipeNutritionValues returns resource error, resource error is returned`() = runTest {
        mockData(nutritionResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns resource error, resource error is returned`() = runTest {
        mockData(diaryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns resource success, resource success is returned`() = runTest {
        val recipeDiaryEntry = mockRecipeDiaryEntry().copy(
            id = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_1,
            nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
            servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2
        )
        mockData()
        assertIs<Resource.Success<Unit>>(callTestedMethod())
        // TODO: Better test nutrition values
        coVerify(exactly = 1) {
            diaryRepository.editRecipeDiaryEntry(
                userId = MockConstants.USER_ID_1,
                newNutritionValues = recipeDiaryEntry.nutritionValues,
                newServings = recipeDiaryEntry.servings,
                recipeDiaryEntryId = recipeDiaryEntry.id
            )
        }
    }

    private fun mockData(
        recipeDiaryEntryResource: Resource<RecipeDiaryEntry?> = Resource.Success(mockRecipeDiaryEntry()),
        recipeResource: Resource<Recipe?> = Resource.Success(MockConstants.Diary.getSampleRecipe()),
        nutritionResource: Resource<NutritionValues> = Resource.Success(MockConstants.Diary.getSampleNutritionValues()),
        diaryResource: Resource<Unit> = Resource.Success(Unit)
    ) {
        coEvery { getRecipeDiaryEntryUseCase(recipeDiaryEntryId = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_1) } returns recipeDiaryEntryResource
        coEvery { getRecipeUseCase(recipeId = MockConstants.Diary.RECIPE_ID_1) } returns recipeResource
        coEvery { calculateRecipeNutritionValuesUseCase(recipe = any(), servings = any()) } returns nutritionResource
        coEvery {
            diaryRepository.editRecipeDiaryEntry(
                userId = MockConstants.USER_ID_1,
                newNutritionValues = any(),
                newServings = any(),
                recipeDiaryEntryId = any()
            )
        } returns diaryResource
    }

    private fun mockRecipeDiaryEntry(
        userId: String = MockConstants.USER_ID_1,
        servings: Int = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1,
    ) = RecipeDiaryEntry(
        userId = userId,
        servings = servings,
        recipeId = MockConstants.Diary.RECIPE_ID_1
    )
    private suspend fun callTestedMethod(
        editRecipeDiaryEntryRequest: EditRecipeDiaryEntryRequest = mockEditRecipeDiaryEntryRequest(),
        userId: String = MockConstants.USER_ID_1
    ) = editRecipeDiaryEntryUseCase(
        editRecipeDiaryEntryRequest = editRecipeDiaryEntryRequest,
        userId = userId
    )

    private fun mockEditRecipeDiaryEntryRequest() = EditRecipeDiaryEntryRequest(
        recipeDiaryEntryId = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_1,
        newServings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2
    )
}