package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductUseCase: GetProductUseCase
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntryPostRequest) {
        val product = getProductUseCase(productId = productDiaryEntryPostRequest.productId).data ?: return Resource.Error()

        return if (productDiaryEntryPostRequest.weight <= 0) {
            Resource.Error()
        } else if (productDiaryEntryPostRequest.date.isEmpty()) {
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
                    nutritionValues = product.calculateNutritionValues(weight = productDiaryEntryPostRequest.weight),
                    productId = productDiaryEntryPostRequest.productId,
                    productName = product.name,
                    productMeasurementUnit = product.measurementUnit
                ),
                userId = userId
            )
        }
    }
}