package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CalculateProductNutritionValuesUseCaseTest {

    private val calculateProductNutritionValuesUseCase = CalculateProductNutritionValuesUseCase()

    @Test
    fun `Check if weight is 0, resource error is returned`() = runTest {
        assertIs<Resource.Error<NutritionValues>>(callTestedMethod(weight = MockConstants.Diary.ZERO_PRODUCT_DIARY_ENTRY_WEIGHT))
    }

    @Test
    fun `Check if weight is less than 0, resource error is returned`() = runTest {
        assertIs<Resource.Error<NutritionValues>>(callTestedMethod(weight = MockConstants.Diary.NEGATIVE_PRODUCT_DIARY_ENTRY_WEIGHT))
    }

    @Test
    fun `Check if values are correct and resource success is returned 1`() = runTest {
        val resource = callTestedMethod(product = Product(nutritionValues = MockConstants.Diary.getSampleNutritionValues()))
        assertIs<Resource.Success<NutritionValues>>(resource)
        assertEquals(
            actual = resource.data,
            expected = NutritionValues(
                calories = 63,
                carbohydrates = 7.75,
                protein = 4.25,
                fat = 1.75
            )
        )
    }

    @Test
    fun `Check if values are correct and resource success is returned 2`() = runTest {
        val resource = callTestedMethod(
            product = Product(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues2()
            ),
            weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2
        )
        assertIs<Resource.Success<NutritionValues>>(resource)
        assertEquals(
            actual = resource.data,
            expected = NutritionValues(
                calories = 1431,
                carbohydrates = 103.2,
                protein = 76.15,
                fat = 79.48
            )
        )
    }

    private fun callTestedMethod(
        product: Product = MockConstants.Diary.getSampleProduct(),
        weight: Int = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
    ) = calculateProductNutritionValuesUseCase(
        product = product,
        weight = weight
    )
}