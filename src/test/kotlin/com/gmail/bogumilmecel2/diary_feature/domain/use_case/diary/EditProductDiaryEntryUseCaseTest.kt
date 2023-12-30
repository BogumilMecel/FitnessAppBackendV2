package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDateTime
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class EditProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getProductDiaryEntryUseCase = mockkClass(GetProductDiaryEntryUseCase::class)
    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val editProductDiaryEntryUseCase = EditProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getProductDiaryEntryUseCase = getProductDiaryEntryUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if id is null resource error is returned`() = runTest {
        callTestedMethod(productDiaryEntryId = null).assertIsError(InvalidIdException)
    }

    @Test
    fun `Check if nutrition values are null resource error is returned`() = runTest {
        callTestedMethod(productNutritionValues = null).assertIsError(InvalidNutritionValuesException)
    }

    @Test
    fun `Check if weight is null resource error is returned`() = runTest {
        callTestedMethod(productDiaryEntryWeight = null).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if getProductDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Error())
        callTestedMethod().assertIsError(DiaryEntryNotFoundException)
    }

    @Test
    fun `Check if getProductDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Success(data = null))
        callTestedMethod().assertIsError(DiaryEntryNotFoundException)
    }

    @Test
    fun `Check if user id do not match, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(MockConstants.USER_ID_2).assertIsError(ForbiddenException)
    }

    @Test
    fun `Check if new weight is 0 resource error is returned`() = runTest {
        mockData()
        callTestedMethod(productDiaryEntryWeight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if new weight is less than 0 resource error is returned`() = runTest {
        mockData()
        callTestedMethod(productDiaryEntryWeight = MockConstants.Diary.NEGATIVE_VALUE).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if request weight is the same as entry weight, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(productDiaryEntryWeight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1).assertIsError(
            InvalidWeightException
        )
    }

    @Test
    fun `Check if entry date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        callTestedMethod().assertIsError(DateNotInRangeException)
    }

    @Test
    fun `Check if called with correct data and repository returns resource error, resource error is returned`() = runTest {
        mockData(repositoryResource = Resource.Error())
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if called with correct data and repository returns resource success, resource success is returned`() = runTest {
        mockData()
        mockDateTime(dateTime = MockConstants.DATE_TIME_ONE_WEEK_LATER.toLocalDateTime()!!)
        val expectedProductDiaryEntry = MockConstants.Diary.getProductDiaryEntry().copy(
            weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2,
            userId = MockConstants.USER_ID_1,
            changeDateTime = MockConstants.DATE_TIME_ONE_WEEK_LATER.toLocalDateTime()!!
        )
        callTestedMethod().run {
            assertIsSuccess()
            assertEquals(
                actual = data,
                expected = expectedProductDiaryEntry.toProductDiaryEntry()
            )
        }
        coVerify(exactly = 1) { diaryRepository.editProductDiaryEntry(productDiaryEntry = expectedProductDiaryEntry) }
    }

    private fun mockData(
        productDiaryEntryResource: Resource<ProductDiaryEntryDto?> = Resource.Success(data = MockConstants.Diary.getProductDiaryEntry()),
        repositoryResource: Resource<Unit> = Resource.Success(Unit),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { getProductDiaryEntryUseCase(MockConstants.Diary.PRODUCT_DIARY_ENTRY_ID) } returns productDiaryEntryResource
        coEvery { diaryRepository.editProductDiaryEntry(productDiaryEntry = any()) } returns repositoryResource
        coEvery { isDateInValidRangeUseCaseUseCase(date = MockConstants.DATE_TIME.toLocalDateTime()!!.date) } returns isDateInValidRange
    }

    private suspend fun callTestedMethod(
        userId: String = MockConstants.USER_ID_1,
        productDiaryEntryId: String? = MockConstants.Diary.PRODUCT_DIARY_ENTRY_ID,
        productNutritionValues: NutritionValues? = MockConstants.Diary.getNutritionValues(),
        productDiaryEntryWeight: Int? = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2,
    ) = editProductDiaryEntryUseCase(
        productDiaryEntry = MockConstants.Diary.getProductDiaryEntry()
            .toProductDiaryEntry()
            .copy(
                weight = productDiaryEntryWeight,
                nutritionValues = productNutritionValues,
                id = productDiaryEntryId
            ),
        userId = userId
    )
}