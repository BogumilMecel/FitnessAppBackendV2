package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.EditProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
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

class EditProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getProductUseCase = mockkClass(GetProductUseCase::class)
    private val getProductDiaryEntryUseCase = mockkClass(GetProductDiaryEntryUseCase::class)
    private val calculateProductNutritionValuesUseCase = mockkClass(CalculateProductNutritionValuesUseCase::class)
    private val editProductDiaryEntryUseCase = EditProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getProductUseCase = getProductUseCase,
        getProductDiaryEntryUseCase = getProductDiaryEntryUseCase,
        calculateProductNutritionValuesUseCase = calculateProductNutritionValuesUseCase
    )

    @Test
    fun `Check if request weight is less than 0, resource error is returned`() = runTest {
        assertIs<Resource.Error<Unit>>(callTestedMethod(weight = -1))
    }

    @Test
    fun `Check if request weight is 0, resource error is returned`() = runTest {
        assertIs<Resource.Error<Unit>>(callTestedMethod(weight = 0))
    }

    @Test
    fun `Check if getProductDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if getProductDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

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
    fun `Check if calculateNutritionValues returns resource error, resource error is returned`() = runTest {
        mockData(calculateProductNutritionValuesResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if called with correct data and repository returns resource error, resource error is returned`() = runTest {
        mockData(repositoryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if called with correct data and repository returns resource success, resource success is returned`() = runTest {
        mockData()
        assertIs<Resource.Success<Unit>>(
            callTestedMethod(
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2
            )
        )
        val expectedProductDiaryEntry = mockProductDiaryEntry().copy(
            weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2
        )
        coVerify(exactly = 1) {
            diaryRepository.editProductDiaryEntry(
                productDiaryEntry = expectedProductDiaryEntry,
                userId = MockConstants.USER_ID
            )
        }
    }

    private fun mockData(
        productDiaryEntryResource: Resource<ProductDiaryEntry?> = Resource.Success(mockProductDiaryEntry()),
        productResource: Resource<Product?> = Resource.Success(mockProduct()),
        calculateProductNutritionValuesResource: Resource<NutritionValues> = Resource.Success(NutritionValues()),
        repositoryResource: Resource<Unit> = Resource.Success(Unit)
    ) {
        coEvery { getProductDiaryEntryUseCase(MockConstants.Diary.DIARY_ENTRY_ID_21) } returns productDiaryEntryResource
        coEvery { getProductUseCase(productId = MockConstants.Diary.PRODUCT_ID_11) } returns productResource
        coEvery {
            calculateProductNutritionValuesUseCase(
                product = mockProduct(),
                weight = any()
            )
        } returns calculateProductNutritionValuesResource
        coEvery { diaryRepository.editProductDiaryEntry(productDiaryEntry = any(), userId = MockConstants.USER_ID) } returns repositoryResource
    }

    private suspend fun callTestedMethod(weight: Int = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1) = editProductDiaryEntryUseCase(
        editProductDiaryEntryRequest = mockEditProductDiaryEntryRequest(weight = weight),
        userId = MockConstants.USER_ID
    )

    private fun mockEditProductDiaryEntryRequest(weight: Int) = EditProductDiaryEntryRequest(
        productDiaryEntryId = MockConstants.Diary.DIARY_ENTRY_ID_21,
        newWeight = weight
    )

    private fun mockProduct() = Product(
        id = MockConstants.Diary.PRODUCT_ID_11
    )

    private fun mockProductDiaryEntry() = ProductDiaryEntry(
        productId = MockConstants.Diary.PRODUCT_ID_11,
        weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
    )
}