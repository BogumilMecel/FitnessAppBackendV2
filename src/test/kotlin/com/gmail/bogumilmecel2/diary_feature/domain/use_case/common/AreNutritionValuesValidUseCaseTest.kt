package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AreNutritionValuesValidUseCaseTest {

    private val areNutritionValuesValidUseCase = AreNutritionValuesValidUseCase()

    @Test
    fun `Check if calories are less than 0 false is returned`() {
        assertFalse { callTestedMethod(calories = MockConstants.Diary.NEGATIVE_VALUE) }
    }

    @Test
    fun `Check if carbohydrates are less than 0 false is returned`() {
        assertFalse { callTestedMethod(carbohydrates = MockConstants.Diary.NEGATIVE_VALUE.toDouble()) }
    }

    @Test
    fun `Check if protein are less than 0 false is returned`() {
        assertFalse { callTestedMethod(protein = MockConstants.Diary.NEGATIVE_VALUE.toDouble()) }
    }

    @Test
    fun `Check if fat are less than 0 false is returned`() {
        assertFalse { callTestedMethod(fat = MockConstants.Diary.NEGATIVE_VALUE.toDouble()) }
    }

    @Test
    fun `Check if called with proper values true is returned`() {
        assertTrue { callTestedMethod() }
    }

    private fun callTestedMethod(
        calories: Int = 460,
        carbohydrates: Double = 40.0,
        protein: Double = 30.0,
        fat: Double = 20.0
    ) = areNutritionValuesValidUseCase(
        nutritionValues = NutritionValues(
            calories = calories,
            carbohydrates = carbohydrates,
            protein = protein,
            fat = fat
        )
    )
}