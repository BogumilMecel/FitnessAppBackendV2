package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewProductRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.AreNutritionValuesValid
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class InsertProductUseCaseTest : BaseTest() {

    private val diaryRepository = mockk<DiaryRepository>()
    private val getUsernameUseCase = mockkClass(GetUsernameUseCase::class)
    private val isDiaryNameValidUseCase = mockkClass(IsDiaryNameValidUseCase::class)
    private val areNutritionValuesValid = mockkClass(AreNutritionValuesValid::class)
    private val calculateNutritionValuesUseCase = mockkClass(CalculateNutritionValuesUseCase::class)
    private val insertProductUseCase = InsertProductUseCase(
        diaryRepository = diaryRepository,
        getUsernameUseCase = getUsernameUseCase,
        isDiaryNameValidUseCase = isDiaryNameValidUseCase,
        areNutritionValuesValid = areNutritionValuesValid,
        calculateNutritionValuesUseCase = calculateNutritionValuesUseCase
    )

    @Test
    fun `Check if username is not valid, resource error is returned`() = runTest {
        mockClasses(isDiaryNameValid = false)
        assertIs<Resource.Error<Product>>(callTestedUseCase())
    }

    @Test
    fun `Check if nutrition values are in 100g and container weight is not null but is less than 0, resource error is returned`() =
        runTest {
            mockClasses()
            assertIs<Resource.Error<Product>>(
                callTestedUseCase(
                    request = mockNewProductRequest(
                        containerWeight = MockConstants.Diary.NEGATIVE_PRODUCT_DIARY_ENTRY_WEIGHT
                    )
                )
            )
        }

    @Test
    fun `Check if nutrition values are in 100g and container weight is not null but is 0, resource error is returned`() = runTest {
        mockClasses()
        assertIs<Resource.Error<Product>>(
            callTestedUseCase(
                request = mockNewProductRequest(
                    containerWeight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT
                )
            )
        )
    }

    @Test
    fun `Check if nutrition values are in container and container weight is null, resource error is returned`() = runTest {
        mockClasses()
        assertIs<Resource.Error<Product>>(
            callTestedUseCase(
                request = mockNewProductRequest(
                    containerWeight = null,
                    nutritionValuesIn = NutritionValuesIn.CONTAINER
                )
            )
        )
    }

    @Test
    fun `Check if nutrition values are in average and serving weight is null, resource error is returned`() = runTest {
        mockClasses()
        assertIs<Resource.Error<Product>>(
            callTestedUseCase(
                request = mockNewProductRequest(
                    containerWeight = null,
                    nutritionValuesIn = NutritionValuesIn.AVERAGE
                )
            )
        )
    }

    @Test
    fun `Check if nutrition values are not in 100g and container weight is less than 0, resource error is returned`() = runTest {
        mockClasses()
        listOf(NutritionValuesIn.CONTAINER, NutritionValuesIn.AVERAGE).forEach {
            assertIs<Resource.Error<Product>>(
                callTestedUseCase(
                    request = mockNewProductRequest(
                        containerWeight = MockConstants.Diary.NEGATIVE_PRODUCT_DIARY_ENTRY_WEIGHT,
                        nutritionValuesIn = it
                    )
                )
            )
        }
    }

    @Test
    fun `Check if nutrition values are not in 100g and container weight is 0, resource error is returned`() = runTest {
        mockClasses()
        listOf(NutritionValuesIn.CONTAINER, NutritionValuesIn.AVERAGE).forEach {
            assertIs<Resource.Error<Product>>(
                callTestedUseCase(
                    request = mockNewProductRequest(
                        containerWeight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT,
                        nutritionValuesIn = it
                    )
                )
            )
        }
    }

    @Test
    fun `Check if nutrition values are not valid, resource error is returned`() = runTest {
        mockClasses(areNutritionValuesValid = false)
        assertIs<Resource.Error<Product>>(callTestedUseCase())
    }

    @Test
    fun `Check if barcode is not null and is longer than maximum, resource error is returned`() = runTest {
        mockClasses()
        assertIs<Resource.Error<Product>>(
            callTestedUseCase(
                request = mockNewProductRequest(
                    barcode = "A".repeat(Constants.Diary.BARCODE_MAX_LENGTH + 1)
                )
            )
        )
    }

    @Test
    fun `Check if barcode is not null and is empty, resource error is returned`() = runTest {
        mockClasses()
        assertIs<Resource.Error<Product>>(
            callTestedUseCase(
                request = mockNewProductRequest(
                    barcode = ""
                )
            )
        )
    }

    @Test
    fun `Check if get username returns null, resource error is returned`() = runTest {
        mockClasses(username = null)
        assertIs<Resource.Error<Product>>(callTestedUseCase())
    }

    @Test
    fun `Check if nutrition values are not calculated if weight is in 100g`() = runTest {
        mockClasses()
        callTestedUseCase()
        verify(exactly = 0) {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        }
    }

    @Test
    fun `Check if nutrition values are calculated if weight is in average`() = runTest {
        mockClasses()
        callTestedUseCase(
            request = mockNewProductRequest(
                nutritionValuesIn = NutritionValuesIn.AVERAGE,
                containerWeight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        )
        verify(exactly = 1) {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        }
    }

    @Test
    fun `Check if nutrition values are calculated if weight is in container`() = runTest {
        mockClasses()
        callTestedUseCase(
            request = mockNewProductRequest(
                nutritionValuesIn = NutritionValuesIn.CONTAINER,
                containerWeight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        )
        verify(exactly = 1) {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        }
    }

    @Test
    fun `Check if request is correct, and repository return resource error, resource error is returned`() = runTest {
        mockClasses(repositoryResource = Resource.Error())
        assertIs<Resource.Error<Product>>(callTestedUseCase())
    }

    @Test
    fun `Check if request is correct, and repository return resource success, resource success is returned`() = runTest {
        mockClasses()
        val timestamp = MockConstants.TIMESTAMP
        val nutritionValuesIn = NutritionValuesIn.CONTAINER
        val containerWeight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
        mockLocalDate(utcTimestamp = timestamp)
        val resource = callTestedUseCase(
            request = mockNewProductRequest(
                nutritionValuesIn = nutritionValuesIn,
                containerWeight = containerWeight,
            )
        )
        verify(exactly = 1) {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
                weight = containerWeight
            )
        }
        assertIs<Resource.Success<Product>>(resource)
        coVerify(exactly = 1) {
            diaryRepository.insertProduct(
                country = Country.POLAND,
                userId = MockConstants.USER_ID_1,
                product = Product(
                    name = MockConstants.Diary.PRODUCT_NAME_1,
                    barcode = MockConstants.Diary.BARCODE,
                    containerWeight = containerWeight,
                    utcTimestamp = timestamp,
                    nutritionValuesIn = nutritionValuesIn,
                    measurementUnit = MeasurementUnit.GRAMS,
                    nutritionValues = MockConstants.Diary.getSampleNutritionValues2(),
                    username = MockConstants.USERNAME,
                    userId = MockConstants.USER_ID_1
                )
            )
        }
        assertIs<Resource.Success<Product>>(callTestedUseCase())
    }

    private fun mockClasses(
        isDiaryNameValid: Boolean = true,
        areNutritionValuesValid: Boolean = true,
        username: String? = MockConstants.USERNAME,
        nutritionValuesResource: Resource<NutritionValues> = Resource.Success(MockConstants.Diary.getSampleNutritionValues2()),
        repositoryResource: Resource<Product> = Resource.Success(data = MockConstants.Diary.getSampleProduct())
    ) {
        every { isDiaryNameValidUseCase(MockConstants.Diary.PRODUCT_NAME_1) } returns isDiaryNameValid
        every { areNutritionValuesValid(MockConstants.Diary.getSampleNutritionValues()) } returns areNutritionValuesValid
        coEvery { getUsernameUseCase(userId = MockConstants.USER_ID_1) } returns username
        every {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        } returns nutritionValuesResource
        coEvery {
            diaryRepository.insertProduct(
                country = Country.POLAND,
                userId = MockConstants.USER_ID_1,
                product = any()
            )
        } returns repositoryResource
    }

    private suspend fun callTestedUseCase(
        request: NewProductRequest = mockNewProductRequest()
    ) = insertProductUseCase(
        newProductRequest = request,
        country = Country.POLAND,
        userId = MockConstants.USER_ID_1
    )

    private fun mockNewProductRequest(
        name: String = MockConstants.Diary.PRODUCT_NAME_1,
        nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
        containerWeight: Int? = null,
        barcode: String? = MockConstants.Diary.BARCODE
    ) = NewProductRequest(
        name = name,
        measurementUnit = MeasurementUnit.GRAMS,
        containerWeight = containerWeight,
        barcode = barcode,
        nutritionValuesIn = nutritionValuesIn,
        nutritionValues = MockConstants.Diary.getSampleNutritionValues()
    )
}