package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.constants.ValidationConstants
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.isLengthInRange
import com.gmail.bogumilmecel2.common.util.extensions.isNumberNatural
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewProductRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase
import kotlin.math.roundToInt

class InsertProductUseCase(
    private val diaryRepository: DiaryRepository,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val isDiaryNameValidUseCase: IsDiaryNameValidUseCase
) {
    suspend operator fun invoke(newProductRequest: NewProductRequest, userId: String): Resource<Product> =
        with(newProductRequest) {
            return if (!isDiaryNameValidUseCase(name = name)) {
                Resource.Error()
            } else if (nutritionValuesIn != NutritionValuesIn.HUNDRED_GRAMS && containerWeight <= 0) {
                Resource.Error()
            } else if (!checkIfNutritionValuesAreNaturalNumbers(nutritionValues = nutritionValues)) {
                Resource.Error()
            } else if (barcode != null && barcode.isLengthInRange(maximum = ValidationConstants.Diary.BARCODE_MAX_LENGTH)) {
                Resource.Error()
            } else {
                val username = getUsernameUseCase(userId = userId)
                if (username != null) {
                    val realContainerWeight = containerWeight.let { weight ->
                        if (newProductRequest.nutritionValuesIn == NutritionValuesIn.HUNDRED_GRAMS && weight <= 0) 100 else weight
                    }

                    val product = Product(
                        name = name,
                        barcode = barcode,
                        containerWeight = realContainerWeight,
                        timestamp = timestamp,
                        nutritionValuesIn = nutritionValuesIn,
                        measurementUnit = newProductRequest.measurementUnit,
                        nutritionValues = if (newProductRequest.nutritionValuesIn == NutritionValuesIn.HUNDRED_GRAMS) newProductRequest.nutritionValues else {
                            calculateNutritionValuesIn100g(
                                nutritionValues = newProductRequest.nutritionValues,
                                weight = newProductRequest.containerWeight
                            )
                        },
                        username = username,
                        userId = userId
                    )
                    diaryRepository.insertProduct(product = product, userId = userId)
                } else {
                    Resource.Error()
                }
            }
        }

    private fun checkIfNutritionValuesAreNaturalNumbers(nutritionValues: NutritionValues) = with(nutritionValues) {
        return@with calories.isNumberNatural() && carbohydrates.isNumberNatural() && protein.isNumberNatural() && fat.isNumberNatural()
    }

    private fun calculateNutritionValuesIn100g(nutritionValues: NutritionValues, weight: Int): NutritionValues {
        return nutritionValues.run {
            NutritionValues(
                calories = (calories.toDouble() / weight * 100.0).roundToInt(),
                carbohydrates = (carbohydrates / weight * 100.0).round(2),
                protein = (protein / weight * 100.0).round(2),
                fat = (fat / weight * 100.0).round(2),
            )
        }
    }
}