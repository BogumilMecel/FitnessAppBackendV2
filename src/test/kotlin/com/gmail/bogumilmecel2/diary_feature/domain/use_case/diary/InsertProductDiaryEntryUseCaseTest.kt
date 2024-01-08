package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

class InsertProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val insertProductDiaryEntryUseCase = InsertProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if product id is null resource error is returned`() = runTest {
        callTestedMethod(productId = null).assertIsError(InvalidIdException)
    }

    @Test
    fun `Check if date is null resource error is returned`() = runTest {
        callTestedMethod(date = null).assertIsError(InvalidDateException)
    }

    @Test
    fun `Check if weight is null resource error is returned`() = runTest {
        callTestedMethod(weight = null).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if meal name is null resource error is returned`() = runTest {
        callTestedMethod(mealName = null).assertIsError(InvalidMealNameException)
    }

    @Test
    fun `Check if nutrition values are null resource error is returned`() = runTest {
        callTestedMethod(nutritionValues = null).assertIsError(InvalidNutritionValuesException)
    }

    @Test
    fun `Check if getProduct returns resource error, resource error is returned`() = runTest {
        mockData(productResource = Resource.Error())
        callTestedMethod().assertIsError(ProductNotFoundException)
    }

    @Test
    fun `Check if getProduct returns null, resource error is returned`() = runTest {
        mockData(productResource = Resource.Success(data = null))
        callTestedMethod().assertIsError(ProductNotFoundException)
    }

    @Test
    fun `Check if date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        callTestedMethod().assertIsError(InvalidDateException)
    }

    @Test
    fun `Check if weight is 0, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(weight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if weight is less than 0, resource error is returned`() = runTest {
        mockData()
        callTestedMethod(weight = MockConstants.Diary.NEGATIVE_VALUE).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if repository returns resource error, resource error is returned`() = runTest {
        mockData(insertResource = Resource.Error())
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if repository returns success, resource success is returned and correct data is used`() = runTest {
        mockDateTime(dateTime = MockConstants.getDateTime())
        mockData()
        callTestedMethod().run {
            assertIsSuccess()
            assertEquals(
                expected = MockConstants.Diary.getProductDiaryEntry().toProductDiaryEntry(),
                actual = data
            )
        }
        coVerify(exactly = 1) { diaryRepository.insertProductDiaryEntry(productDiaryEntry = any()) }
    }

    private fun mockData(
        productResource: Resource<ProductDto?> = Resource.Success(MockConstants.Diary.getProduct()),
        insertResource: Resource<ProductDiaryEntryDto> = Resource.Success(MockConstants.Diary.getProductDiaryEntry()),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { diaryRepository.getProduct(productId = MockConstants.Diary.PRODUCT_ID) } returns productResource
        coEvery { diaryRepository.insertProductDiaryEntry(productDiaryEntry = any()) } returns insertResource
        every { isDateInValidRangeUseCaseUseCase(date = MockConstants.getDate()) } returns isDateInValidRange
    }

    private suspend fun callTestedMethod(
        productId: String? = MockConstants.Diary.PRODUCT_ID,
        weight: Int? = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
        mealName: MealName? = MealName.BREAKFAST,
        date: LocalDate? = MockConstants.getDate(),
        nutritionValues: NutritionValues? = MockConstants.Diary.getNutritionValues(),
    ) = insertProductDiaryEntryUseCase(
        productDiaryEntry = MockConstants.Diary.getProductDiaryEntry().toProductDiaryEntry().copy(
            productId = productId,
            weight = weight,
            mealName = mealName,
            date = date,
            nutritionValues = nutritionValues
        ),
        userId = MockConstants.USER_ID_1
    )
}