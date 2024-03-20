package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.AreNutritionValuesValidUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertTrue

class InsertProductUseCaseTest : BaseTest() {

    private val diaryRepository = mockk<DiaryRepository>()
    private val getUsernameUseCase = mockkClass(GetUsernameUseCase::class)
    private val isDiaryNameValidUseCase = mockkClass(IsDiaryNameValidUseCase::class)
    private val areNutritionValuesValidUseCase = mockkClass(AreNutritionValuesValidUseCase::class)
    private val calculateNutritionValuesUseCase = mockkClass(CalculateNutritionValuesUseCase::class)
    private val insertProductUseCase = InsertProductUseCase(
        diaryRepository = diaryRepository,
        getUsernameUseCase = getUsernameUseCase,
        isDiaryNameValidUseCase = isDiaryNameValidUseCase,
        areNutritionValuesValidUseCase = areNutritionValuesValidUseCase,
        calculateNutritionValuesUseCase = calculateNutritionValuesUseCase
    )

    @Test
    fun `Check if name is null resource error is returned`() = runTest {
        callTestedMethod(name = null).assertIsError(InvalidProductNameException)
    }

    @Test
    fun `Check if nutrition values are null resource error is returned`() = runTest {
        callTestedMethod(nutritionValues = null).assertIsError(InvalidNutritionValuesException)
    }

    @Test
    fun `Check if measurement unit is null resource error is returned`() = runTest {
        callTestedMethod(measurementUnit = null).assertIsError(InvalidMeasurementUnit)
    }

    @Test
    fun `Check if nutrition values in is null resource error is returned`() = runTest {
        callTestedMethod(nutritionValuesIn = null).assertIsError(InvalidNutritionValuesInException)
    }

    @Test
    fun `Check if product name is not valid, resource error is returned`() = runTest {
        mockClasses(isDiaryNameValid = false)
        callTestedMethod().assertIsError(InvalidProductNameException)
    }

    @Test
    fun `Check if nutrition values are in 100g and container weight is not null but is less than 0, resource error is returned`() =
        runTest {
            mockClasses()
            callTestedMethod(containerWeight = MockConstants.Diary.NEGATIVE_VALUE).assertIsError(InvalidWeightException)
        }

    @Test
    fun `Check if nutrition values are in 100g and container weight is not null but is 0, resource error is returned`() = runTest {
        mockClasses()
        callTestedMethod(containerWeight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if nutrition values are in container and container weight is null, resource error is returned`() = runTest {
        mockClasses()
        callTestedMethod(containerWeight = null, nutritionValuesIn = NutritionValuesIn.CONTAINER).assertIsError(InvalidWeightException)
    }

    @Test
    fun `Check if nutrition values are in average and serving weight is null, resource error is returned`() = runTest {
        mockClasses()
        callTestedMethod(containerWeight = null, nutritionValuesIn = NutritionValuesIn.AVERAGE).assertIsError(InvalidWeightException)
    }

    @ParameterizedTest
    @EnumSource(NutritionValuesIn::class)
    fun `Check if nutrition values are not in 100g and container weight is less than 0, resource error is returned`(nutritionValuesIn: NutritionValuesIn) = runTest {
        mockClasses()
        callTestedMethod(containerWeight = MockConstants.Diary.NEGATIVE_VALUE, nutritionValuesIn = nutritionValuesIn).run {
            when(nutritionValuesIn) {
                NutritionValuesIn.AVERAGE -> assertIsError(InvalidWeightException)
                NutritionValuesIn.CONTAINER -> assertIsError(InvalidWeightException)
                NutritionValuesIn.HUNDRED_GRAMS -> assertIsSuccess()
                NutritionValuesIn.HUNDRED_MILLILITERS -> assertTrue(true)
            }
        }
    }

    @ParameterizedTest
    @EnumSource(NutritionValuesIn::class)
    fun `Check if nutrition values are not in 100g and container weight is 0, resource error is returned`(nutritionValuesIn: NutritionValuesIn) = runTest {
        mockClasses()
        callTestedMethod(containerWeight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT, nutritionValuesIn = nutritionValuesIn).run {
            when(nutritionValuesIn) {
                NutritionValuesIn.AVERAGE -> assertIsError(InvalidWeightException)
                NutritionValuesIn.CONTAINER -> assertIsError(InvalidWeightException)
                NutritionValuesIn.HUNDRED_GRAMS -> assertIsSuccess()
                NutritionValuesIn.HUNDRED_MILLILITERS -> assertTrue(true)
            }
        }
    }

    @Test
    fun `Check if nutrition values are not valid, resource error is returned`() = runTest {
        mockClasses(areNutritionValuesValid = false)
        callTestedMethod().assertIsError(InvalidNutritionValuesException)
    }

    @Test
    fun `Check if barcode is not null and is longer than maximum, resource error is returned`() = runTest {
        mockClasses()
        callTestedMethod(barcode = "A".repeat(Constants.Diary.BARCODE_MAX_LENGTH + 1)).assertIsError(InvalidBarcodeLengthException)
    }

    @Test
    fun `Check if barcode is not null and is empty, resource error is returned`() = runTest {
        mockClasses()
        callTestedMethod(barcode = "").assertIsError(InvalidBarcodeLengthException)
    }

    @Test
    fun `Check if get username returns null, resource error is returned`() = runTest {
        mockClasses(username = null)
        callTestedMethod().assertIsError(UserNotFoundException)
    }

    @ParameterizedTest
    @EnumSource(NutritionValuesIn::class)
    fun `Check if nutrition values are calculated correctly`(nutritionValuesIn: NutritionValuesIn) = runTest {
        mockClasses()
        callTestedMethod(nutritionValuesIn = nutritionValuesIn, containerWeight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1)
        verify(exactly = if (nutritionValuesIn == NutritionValuesIn.HUNDRED_GRAMS) 0 else 1) {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        }
    }

    @Test
    fun `Check if request is correct, and repository return resource error, resource error is returned`() = runTest {
        mockClasses(repositoryResource = Resource.Error())
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if request is correct, and repository return resource success, resource success is returned`() = runTest {
        mockClasses()
        mockDateTime(dateTime = MockConstants.getDateTime())
        callTestedMethod(nutritionValuesIn = NutritionValuesIn.CONTAINER).assertIsSuccess()
        verify(exactly = 1) {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        }
        coVerify(exactly = 1) { diaryRepository.insertProduct(product = any()) }
    }

    private fun mockClasses(
        isDiaryNameValid: Boolean = true,
        areNutritionValuesValid: Boolean = true,
        username: String? = MockConstants.USERNAME,
        nutritionValuesResource: Resource<NutritionValues> = Resource.Success(MockConstants.Diary.getNutritionValues2()),
        repositoryResource: Resource<Product> = Resource.Success(data = MockConstants.Diary.getProduct().toProduct())
    ) {
        every { isDiaryNameValidUseCase(MockConstants.Diary.PRODUCT_NAME) } returns isDiaryNameValid
        every { areNutritionValuesValidUseCase(MockConstants.Diary.getNutritionValues()) } returns areNutritionValuesValid
        coEvery { getUsernameUseCase(userId = MockConstants.USER_ID_1) } returns username
        every {
            calculateNutritionValuesUseCase(
                nutritionValues = MockConstants.Diary.getNutritionValues(),
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        } returns nutritionValuesResource
        coEvery { diaryRepository.insertProduct(product = any()) } returns repositoryResource
    }

    private suspend fun callTestedMethod(
        name: String? = MockConstants.Diary.PRODUCT_NAME,
        nutritionValues: NutritionValues? = MockConstants.Diary.getNutritionValues(),
        nutritionValuesIn: NutritionValuesIn? = NutritionValuesIn.HUNDRED_GRAMS,
        measurementUnit: MeasurementUnit? = MeasurementUnit.GRAMS,
        containerWeight: Int? = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
        barcode: String? = MockConstants.Diary.BARCODE
    ) = insertProductUseCase(
        product = MockConstants.Diary.getProduct().toProduct().copy(
            containerWeight = containerWeight,
            name = name,
            nutritionValues = nutritionValues,
            nutritionValuesIn = nutritionValuesIn,
            measurementUnit = measurementUnit,
            barcode = barcode
        ),
        country = Country.POLAND,
        userId = MockConstants.USER_ID_1
    )
}