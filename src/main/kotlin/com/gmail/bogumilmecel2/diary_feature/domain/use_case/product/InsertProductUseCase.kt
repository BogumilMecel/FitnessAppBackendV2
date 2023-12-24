package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.domain.model.*
import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.copyType
import com.gmail.bogumilmecel2.common.util.extensions.isLengthInRange
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewProductRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NutritionValuesIn
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.AreNutritionValuesValidUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase
import org.bson.types.ObjectId

class InsertProductUseCase(
    private val diaryRepository: DiaryRepository,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val isDiaryNameValidUseCase: IsDiaryNameValidUseCase,
    private val areNutritionValuesValidUseCase: AreNutritionValuesValidUseCase,
    private val calculateNutritionValuesUseCase: CalculateNutritionValuesUseCase
) {
    suspend operator fun invoke(newProductRequest: NewProductRequest, userId: String, country: Country): Resource<Product> =
        with(newProductRequest) {
            if (!isDiaryNameValidUseCase(name = name)) return Resource.Error(exception = InvalidProductNameException)

            when (nutritionValuesIn) {
                NutritionValuesIn.HUNDRED_GRAMS -> {
                    if (containerWeight != null && containerWeight <= 0) return Resource.Error(exception = InvalidWeightException)
                }

                else -> {
                    if (containerWeight == null || containerWeight <= 0) return Resource.Error(exception = InvalidWeightException)
                }
            }

            if (!areNutritionValuesValidUseCase(nutritionValues = nutritionValues)) return Resource.Error(exception = InvalidNutritionValuesException)

            if (barcode != null) {
                if (
                    !barcode.isLengthInRange(
                        maximum = Constants.Diary.BARCODE_MAX_LENGTH,
                        minimum = Constants.Diary.BARCODE_MIN_LENGTH
                    )
                ) return Resource.Error(exception = InvalidBarcodeLengthException)
            }

            val username = getUsernameUseCase(userId = userId) ?: return Resource.Error(exception = CouldNotFindUserException)

            val nutritionValues = when (nutritionValuesIn) {
                NutritionValuesIn.HUNDRED_GRAMS -> nutritionValues
                else -> {
                    val calculateResource = calculateNutritionValuesUseCase(
                        nutritionValues = nutritionValues,
                        weight = containerWeight ?: 100
                    )
                    when (calculateResource) {
                        is Resource.Error -> return calculateResource.copyType()
                        is Resource.Success -> calculateResource.data
                    }
                }
            }

            return diaryRepository.insertProduct(
                product = ProductDto(
                    _id = ObjectId(),
                    name = name,
                    barcode = barcode,
                    containerWeight = containerWeight,
                    creationDateTime = CustomDateUtils.getUtcDateTime(),
                    nutritionValuesIn = nutritionValuesIn,
                    measurementUnit = measurementUnit,
                    nutritionValues = nutritionValues,
                    username = username,
                    userId = userId,
                    country = country
                )
            )
        }
}