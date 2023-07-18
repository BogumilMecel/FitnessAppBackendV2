package com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe

import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CalculateRecipeNutritionValuesUseCaseTest {

    private val calculateRecipeNutritionValuesUseCase = CalculateRecipeNutritionValuesUseCase()

    @Test
    fun `Check if servings are 0, resource error is returned`() = runTest {
        assertIs<Resource.Error<NutritionValues>>(callTestedMethod(servings = MockConstants.Diary.ZERO_RECIPE_SERVINGS))
    }

    @Test
    fun `Check if servings are less than 0, resource error is returned`() = runTest {
        assertIs<Resource.Error<NutritionValues>>(callTestedMethod(servings = MockConstants.Diary.NEGATIVE_RECIPE_SERVINGS))
    }

    @Test
    fun `Check if values are correct and resource success is returned 1`() = runTest {
        val resource = callTestedMethod(
            recipe = Recipe(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues(),
                servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2
            )
        )
        assertIs<Resource.Success<NutritionValues>>(resource)
        assertEquals(
            actual = resource.data,
            expected = NutritionValues(
                calories = 170,
                carbohydrates = 20.67,
                protein = 11.33,
                fat = 4.67
            )
        )
    }

    @Test
    fun `Check if values are correct and resource success is returned 2`() = runTest {
        val resource = callTestedMethod(
            servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_2,
            recipe = Recipe(
                nutritionValues = MockConstants.Diary.getSampleNutritionValues2(),
                servings = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1
            )
        )
        assertIs<Resource.Success<NutritionValues>>(resource)
        assertEquals(
            actual = resource.data,
            expected = NutritionValues(
                calories = 4294,
                carbohydrates = 309.6,
                protein = 228.45,
                fat = 238.46
            )
        )
    }

    private fun callTestedMethod(
        recipe: Recipe = MockConstants.Diary.getSampleRecipe(),
        servings: Int = MockConstants.Diary.CORRECT_RECIPE_SERVINGS_1
    ) = calculateRecipeNutritionValuesUseCase(
        recipe = recipe,
        servings = servings
    )
}