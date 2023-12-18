package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductUseCase: GetProductUseCase,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntryPostRequest) {
        val product = getProductUseCase(productId = productDiaryEntryPostRequest.productId).data ?: return Resource.Error()

        if (weight <= 0) return Resource.Error()
        if (isDateInValidRangeUseCaseUseCase(date)) return Resource.Error()

        val currentDate = CustomDateUtils.getCurrentUtcLocalDateTime()

        diaryRepository.insertProductDiaryEntry(
            productDiaryEntry = ProductDiaryEntry(
                weight = weight,
                mealName = mealName,
                date = date,
                userId = userId,
                nutritionValues = nutritionValues,
                productId = product.id,
                productName = product.name,
                productMeasurementUnit = product.measurementUnit,
                creationDate = currentDate,
                changeDate = currentDate
            ),
            userId = userId
        )
    }
}