package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDate
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDateTime
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class InsertProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getProductUseCase = mockkClass(GetProductUseCase::class)
    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val insertProductDiaryEntryUseCase = InsertProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getProductUseCase = getProductUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if getProduct returns resource error, resource error is returned`() = runTest {
        mockData(productResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if getProduct returns null, resource error is returned`() = runTest {
        mockData(productResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if date is not in valid range, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if weight is 0, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                productDiaryEntryPostRequest = mockProductDiaryEntryPostRequest(
                    weight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT
                )
            )
        )
    }

    @Test
    fun `Check if weight is less than 0, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                productDiaryEntryPostRequest = mockProductDiaryEntryPostRequest(
                    weight = MockConstants.Diary.NEGATIVE_VALUE
                )
            )
        )
    }

    @Test
    fun `Check if repository returns resource error, resource error is returned`() = runTest {
        mockData()
        mockRepositoryResponse()
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns success, resource success is returned and correct data is used`() = runTest {
        val product = MockConstants.Diary.getSampleProduct()
        val productDiaryEntryPostRequest = mockProductDiaryEntryPostRequest()
        mockDateTime(dateTime = MockConstants.DATE_TIME.toLocalDateTime()!!)
        mockData()
        val expectedProductDiaryEntry = ProductDiaryEntry(
            weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
            mealName = productDiaryEntryPostRequest.mealName,
            creationDateTime = MockConstants.DATE_TIME.toLocalDateTime(),
            date = productDiaryEntryPostRequest.date,
            userId = MockConstants.USER_ID_1,
            nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
            productId = productDiaryEntryPostRequest.productId,
            productName = product.name,
            productMeasurementUnit = product.measurementUnit,
            changeDateTime = MockConstants.DATE_TIME.toLocalDateTime()
        )
        mockRepositoryResponse(Resource.Success(expectedProductDiaryEntry))
        assertIs<Resource.Success<Unit>>(callTestedMethod(productDiaryEntryPostRequest = productDiaryEntryPostRequest))
        coVerify(exactly = 1) {
            diaryRepository.insertProductDiaryEntry(
                productDiaryEntry = expectedProductDiaryEntry,
                userId = MockConstants.USER_ID_1
            )
        }
    }

    private fun mockData(
        productResource: Resource<Product?> = Resource.Success(MockConstants.Diary.getSampleProduct()),
        isDateInValidRange: Boolean = true
    ) {
        coEvery { getProductUseCase(productId = MockConstants.Diary.PRODUCT_ID_11) } returns productResource
        every { isDateInValidRangeUseCaseUseCase(date = MockConstants.DATE.toLocalDate()!!) } returns isDateInValidRange
    }

    private fun mockRepositoryResponse(resource: Resource<ProductDiaryEntry> = Resource.Error()) {
        coEvery {
            diaryRepository.insertProductDiaryEntry(
                productDiaryEntry = any(),
                userId = MockConstants.USER_ID_1
            )
        } returns resource
    }

    private suspend fun callTestedMethod(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest = mockProductDiaryEntryPostRequest()
    ) = insertProductDiaryEntryUseCase(
        productDiaryEntryPostRequest = productDiaryEntryPostRequest,
        userId = MockConstants.USER_ID_1
    )

    private fun mockProductDiaryEntryPostRequest(
        weight: Int = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
        nutritionValues: NutritionValues = MockConstants.Diary.getSampleNutritionValues()
    ) = ProductDiaryEntryPostRequest(
        productId = MockConstants.Diary.PRODUCT_ID_11,
        weight = weight,
        mealName = MealName.BREAKFAST,
        date = MockConstants.DATE.toLocalDate()!!,
        nutritionValues = nutritionValues
    )
}