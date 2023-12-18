package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsTimestampInTwoWeeksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class EditRecipeDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getRecipeDiaryEntryUseCase = mockkClass(GetRecipeDiaryEntryUseCase::class)
    private val editRecipeDiaryEntryUseCase = EditRecipeDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getRecipeDiaryEntryUseCase = getRecipeDiaryEntryUseCase,
        isTimestampInTwoWeeksUseCase = IsTimestampInTwoWeeksUseCase()
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
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                userId = MockConstants.USER_ID_2
            )
        )
    }

    @Test
    fun `Check if request servings are the same as entry servings, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1
            )
        )
    }

    @Test
    fun `Check if entry is older than 2 weeks, resource error is returned`() = runTest {
        mockLocalDate(utcTimestamp = MockConstants.TIMESTAMP_MORE_THAN_2_LATER)
        mockData()
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
            nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
            servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2,
            utcTimestamp = MockConstants.TIMESTAMP
        )
        mockLocalDate(utcTimestamp = MockConstants.TIMESTAMP_1_WEEKS_LATER)
        mockData()
        assertIs<Resource.Success<Unit>>(callTestedMethod())
        coVerify(exactly = 1) {
            diaryRepository.editRecipeDiaryEntry(
                recipeDiaryEntry = recipeDiaryEntry.copy(
                    lastEditedUtcTimestamp = MockConstants.TIMESTAMP_1_WEEKS_LATER
                ),
                userId = MockConstants.USER_ID_1,
            )
        }
    }

    private fun mockData(
        recipeDiaryEntryResource: Resource<RecipeDiaryEntry?> = Resource.Success(mockRecipeDiaryEntry()),
        diaryResource: Resource<Unit> = Resource.Success(Unit)
    ) {
        coEvery { getRecipeDiaryEntryUseCase(recipeDiaryEntryId = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_41) } returns recipeDiaryEntryResource
        coEvery { diaryRepository.editRecipeDiaryEntry(recipeDiaryEntry = any(), userId = MockConstants.USER_ID_1) } returns diaryResource
    }

    private fun mockRecipeDiaryEntry(
        userId: String = MockConstants.USER_ID_1,
        servings: Int = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1,
    ) = RecipeDiaryEntry(
        id = MockConstants.Diary.RECIPE_DIARY_ENTRY_ID_41,
        userId = userId,
        servings = servings,
        recipeId = MockConstants.Diary.RECIPE_ID_31,
        utcTimestamp = MockConstants.TIMESTAMP,
        nutritionValues = MockConstants.Diary.getSampleNutritionValues()
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