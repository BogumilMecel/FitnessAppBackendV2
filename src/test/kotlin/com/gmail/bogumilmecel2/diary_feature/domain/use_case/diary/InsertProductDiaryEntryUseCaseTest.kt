package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateProductNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class InsertProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getProductUseCase = mockkClass(GetProductUseCase::class)
    private val calculateProductNutritionValuesUseCase = mockkClass(CalculateProductNutritionValuesUseCase::class)
    private val insertProductDiaryEntryUseCase = InsertProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getProductUseCase = getProductUseCase,
        calculateProductNutritionValuesUseCase = calculateProductNutritionValuesUseCase
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
    fun `Check if request date is empty, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                productDiaryEntryPostRequest = mockProductDiaryEntryPostRequest(date = "")
            )
        )
    }

    @Test
    fun `Check if request date is invalid, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                productDiaryEntryPostRequest = mockProductDiaryEntryPostRequest(date = "abc")
            )
        )
    }

    @Test
    fun `Check if calculateProductNutritionValues returns resource error, resource error is returned`() = runTest {
        mockData(calculateProductNutritionValuesResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns resource error, resource error is returned`() = runTest {
        mockData(repositoryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if repository returns success, resource success is returned and correct data is used`() = runTest {
        val product = MockConstants.Diary.getSampleProduct()
        val nutritionValues = MockConstants.Diary.getSampleNutritionValues()
        val productDiaryEntryPostRequest = mockProductDiaryEntryPostRequest()
        mockLocalDate(utcTimestamp = MockConstants.TIMESTAMP)
        mockData()
        assertIs<Resource.Success<Unit>>(callTestedMethod(productDiaryEntryPostRequest = productDiaryEntryPostRequest))
        coVerify(exactly = 1) {
            diaryRepository.insertProductDiaryEntry(
                productDiaryEntry = ProductDiaryEntry(
                    weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
                    mealName = productDiaryEntryPostRequest.mealName,
                    utcTimestamp = MockConstants.TIMESTAMP,
                    date = productDiaryEntryPostRequest.date,
                    userId = MockConstants.USER_ID_1,
                    nutritionValues = nutritionValues,
                    productId = productDiaryEntryPostRequest.productId,
                    productName = product.name,
                    productMeasurementUnit = product.measurementUnit,
                    lastEditedUtcTimestamp = MockConstants.TIMESTAMP
                ),
                userId = MockConstants.USER_ID_1
            )
        }
    }

    private fun mockData(
        productResource: Resource<Product?> = Resource.Success(MockConstants.Diary.getSampleProduct()),
        repositoryResource: Resource<Unit> = Resource.Success(Unit),
        calculateProductNutritionValuesResource: Resource<NutritionValues> = Resource.Success(MockConstants.Diary.getSampleNutritionValues())
    ) {
        coEvery { getProductUseCase(productId = MockConstants.Diary.PRODUCT_ID_1) } returns productResource
        coEvery { diaryRepository.insertProductDiaryEntry(productDiaryEntry = any(), userId = MockConstants.USER_ID_1) } returns repositoryResource
        coEvery {
            calculateProductNutritionValuesUseCase(
                product = MockConstants.Diary.getSampleProduct(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        } returns calculateProductNutritionValuesResource
    }

    private suspend fun callTestedMethod(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest = mockProductDiaryEntryPostRequest()
    ) = insertProductDiaryEntryUseCase(
        productDiaryEntryPostRequest = productDiaryEntryPostRequest,
        userId = MockConstants.USER_ID_1
    )

    private fun mockProductDiaryEntryPostRequest(
        weight: Int = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
        date: String = MockConstants.MOCK_DATE_2021
    ) = ProductDiaryEntryPostRequest(
        productId = MockConstants.Diary.PRODUCT_ID_1,
        weight = weight,
        mealName = MealName.BREAKFAST,
        date = date
    )
}