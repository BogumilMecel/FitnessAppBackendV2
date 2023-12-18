package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateProductNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductUseCase: GetProductUseCase,
    private val calculateProductNutritionValuesUseCase: CalculateProductNutritionValuesUseCase
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntryPostRequest) {
        val product = getProductUseCase(productId = productDiaryEntryPostRequest.productId).data ?: return Resource.Error()

        return if (productDiaryEntryPostRequest.date.isEmpty()) {
            Resource.Error()
        } else if (!productDiaryEntryPostRequest.date.isValidDate()) {
            Resource.Error()
        } else {
            diaryRepository.insertDiaryEntry(
                productDiaryEntry = ProductDiaryEntry(
                    weight = productDiaryEntryPostRequest.weight,
                    mealName = productDiaryEntryPostRequest.mealName,
                    utcTimestamp = CustomDateUtils.getCurrentUtcTimestamp(),
                    date = productDiaryEntryPostRequest.date,
                    userId = userId,
                    nutritionValues = calculateProductNutritionValuesUseCase(
                        product = product,
                        weight = productDiaryEntryPostRequest.weight
                    ).data ?: return Resource.Error(),
                    productId = productDiaryEntryPostRequest.productId,
                    productName = product.name,
                    productMeasurementUnit = product.measurementUnit
                ),
                userId = userId
            )
        }
    }
}