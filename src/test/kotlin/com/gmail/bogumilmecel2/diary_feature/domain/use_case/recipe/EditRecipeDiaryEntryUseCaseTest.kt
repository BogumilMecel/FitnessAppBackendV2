package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDate
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDateTime
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class EditRecipeDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getRecipeDiaryEntryUseCase = mockkClass(GetRecipeDiaryEntryUseCase::class)
    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val editRecipeDiaryEntryUseCase = EditRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getRecipeDiaryEntryUseCase = getRecipeDiaryEntryUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if getRecipeDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Error())
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if getRecipeDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if user id do not match, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<RecipeDiaryEntry>>(
            callTestedMethod(
                userId = MockConstants.USER_ID_2
            )
        )
    }

    @Test
    fun `Check if request servings are the same as entry servings, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod(servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1))
    }

    @Test
    fun `Check if entry date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if entry does not have date , resource error is returned`() = runTest {
        mockData(recipeDiaryEntryResource = Resource.Success(mockRecipeDiaryEntry(date = null)))
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns resource error, resource error is returned`() = runTest {
        mockData(diaryResource = Resource.Error())
        assertIs<Resource.Error<RecipeDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns resource success, resource success is returned`() = runTest {
        val expectedRecipeDiaryEntry = mockRecipeDiaryEntry().copy(
            nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
            servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2,
            changeDateTime = MockConstants.DATE_TIME.toLocalDateTime()!!,
        )
        mockDateTime(dateTime = MockConstants.DATE_TIME.toLocalDateTime()!!)
        mockData()
        val resource = callTestedMethod()
        resource.assertIsSuccess()
        assertEquals(
            actual = resource.data,
            expected = expectedRecipeDiaryEntry
        )
        coVerify(exactly = 1) {
            diaryRepository.editRecipeDiaryEntry(
                recipeDiaryEntry = expectedRecipeDiaryEntry,
                userId = MockConstants.USER_ID_1,
            )
        }
    }

    private fun mockData(
        recipeDiaryEntryResource: Resource<RecipeDiaryEntry?> = Resource.Success(mockRecipeDiaryEntry()),
        diaryResource: Resource<Unit> = Resource.Success(Unit),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { getRecipeDiaryEntryUseCase(recipeDiaryEntryId = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_41) } returns recipeDiaryEntryResource
        coEvery { diaryRepository.editRecipeDiaryEntry(recipeDiaryEntry = any(), userId = MockConstants.USER_ID_1) } returns diaryResource
        coEvery { isDateInValidRangeUseCaseUseCase(date = MockConstants.DATE.toLocalDate()!!) } returns isDateInValidRange
    }

    private fun mockRecipeDiaryEntry(
        userId: String = MockConstants.USER_ID_1,
        servings: Int = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1,
        date: LocalDate? = MockConstants.DATE.toLocalDate()!!
    ) = RecipeDiaryEntry(
        id = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_41,
        userId = userId,
        servings = servings,
        recipeId = MockConstants.Diary.RECIPE_ID_31,
        date = date,
        nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
    )

    private suspend fun callTestedMethod(
        servings: Int = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2,
        userId: String = MockConstants.USER_ID_1
    ) = editRecipeDiaryEntryUseCase(
        recipeDiaryEntry = mockRecipeDiaryEntry().copy(
            servings = servings,
            userId = userId
        ),
        userId = userId
    )
}