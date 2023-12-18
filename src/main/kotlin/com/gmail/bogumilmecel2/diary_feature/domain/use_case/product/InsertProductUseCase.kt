package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.isLengthInRange
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewProductRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.AreNutritionValuesValid
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase

class InsertProductUseCase(
    private val diaryRepository: DiaryRepository,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val isDiaryNameValidUseCase: IsDiaryNameValidUseCase,
    private val areNutritionValuesValid: AreNutritionValuesValid,
    private val calculateNutritionValuesUseCase: CalculateNutritionValuesUseCase
) {
    suspend operator fun invoke(newProductRequest: NewProductRequest, userId: String, country: Country): Resource<Product> =
        with(newProductRequest) {
            if (!isDiaryNameValidUseCase(name = name)) return Resource.Error()

            when (nutritionValuesIn) {
                NutritionValuesIn.HUNDRED_GRAMS -> {
                    if (containerWeight != null && containerWeight <= 0) return Resource.Error()
                }

                else -> {
                    if (containerWeight == null || containerWeight <= 0) return Resource.Error()
                }
            }

            if (!areNutritionValuesValid(nutritionValues = nutritionValues)) return Resource.Error()

            if (barcode != null) {
                if (!barcode.isLengthInRange(maximum = Constants.Diary.BARCODE_MAX_LENGTH)) return Resource.Error()
            }

            val username = getUsernameUseCase(userId = userId) ?: return Resource.Error()

            val nutritionValues = when (nutritionValuesIn) { 
                NutritionValuesIn.HUNDRED_GRAMS -> nutritionValues
                else -> {
                    calculateNutritionValuesUseCase(nutritionValues = nutritionValues, weight = containerWeight ?: 100).data ?: return Resource.Error()
                }
            }

            return diaryRepository.insertProduct(
                product = Product(
                    name = name,
                    barcode = barcode,
                    containerWeight = containerWeight,
                    utcTimestamp = CustomDateUtils.getCurrentUtcTimestamp(),
                    nutritionValuesIn = nutritionValuesIn,
                    measurementUnit = measurementUnit,
                    nutritionValues = nutritionValues,
                    username = username,
                    userId = userId
                ),
                userId = userId,
                country = country
            )
        }
}