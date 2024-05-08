package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.toRecipe
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class EditRecipeDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val editRecipeDiaryEntryUseCase = EditRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if getRecipeDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Error())
        callTestedMethod().assertIsError(DiaryEntryNotFoundException)
    }

    @Test
    fun `Check if getRecipeDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Success(data = null))
        callTestedMethod().assertIsError(DiaryEntryNotFoundException)
    }

    @Test
    fun `Check if user id do not match, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(userId = MockConstants.USER_ID_2).assertIsError(ForbiddenException)
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
    fun `Check if servings are the same as entry servings, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1).assertIsError(InvalidServingsException)
    }

    @Test
    fun `Check if entry date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        callTestedMethod().assertIsError(InvalidDateException)
    }

    @Test
    fun `Check if repository returns resource error, resource error is returned`() = runTest {
        mockData(diaryResource = Resource.Error())
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns resource success, resource success is returned`() = runTest {
        val expectedRecipeDiaryEntry = MockConstants.Diary.getRecipeDiaryEntry().copy(
            nutritionValues = MockConstants.Diary.getNutritionValues2(),
            servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2
        )
        mockDateTime(dateTime = MockConstants.getDateTimeOneWeekLater())
        mockData()
        callTestedMethod().run {
            assertIsSuccess()
            assertEquals(
                actual = data,
                expected = expectedRecipeDiaryEntry.toRecipe()
            )
        }
        coVerify(exactly = 1) { diaryRepository.editRecipeDiaryEntry(recipeDiaryEntry = expectedRecipeDiaryEntry) }
    }

    private fun mockData(
        recipeDiaryEntryResource: Resource<RecipeDiaryEntryDto?> = Resource.Success(MockConstants.Diary.getRecipeDiaryEntry()),
        diaryResource: Resource<Unit> = Resource.Success(Unit),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { diaryRepository.getRecipeDiaryEntry(id = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID) } returns recipeDiaryEntryResource
        coEvery { diaryRepository.editRecipeDiaryEntry(recipeDiaryEntry = any()) } returns diaryResource
        coEvery { isDateInValidRangeUseCaseUseCase(date = MockConstants.getDateTime().date) } returns isDateInValidRange
    }

    private suspend fun callTestedMethod(
        recipeDiaryEntryId: String = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID,
        servings: Int = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2,
        nutritionValues: NutritionValues = MockConstants.Diary.getNutritionValues2(),
        userId: String = MockConstants.USER_ID_1
    ) = editRecipeDiaryEntryUseCase(
        recipeDiaryEntry = MockConstants.Diary.getRecipeDiaryEntry().toRecipe().copy(
            id = recipeDiaryEntryId,
            nutritionValues = nutritionValues,
            servings = servings,
            userId = userId
        ),
        userId = userId
    )
}