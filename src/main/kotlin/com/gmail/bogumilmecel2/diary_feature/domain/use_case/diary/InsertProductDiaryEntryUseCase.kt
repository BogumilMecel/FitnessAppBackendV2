package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import kotlinx.datetime.LocalDate

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductUseCase: GetProductUseCase,
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntryPostRequest) {
        val product = getProductUseCase(productId = productDiaryEntryPostRequest.productId).data ?: return Resource.Error()

        if (weight <= 0) return Resource.Error()
        if (date.isEmpty()) return Resource.Error()
        if (!date.isValidDate()) return Resource.Error()
        if (CustomDateUtils.getDaysFromNow(from = LocalDate.parse(date)) > Constants.Diary.MAXIMUM_MODIFY_DATE) return Resource.Error()

        val currentTimestamp = CustomDateUtils.getCurrentUtcTimestamp()

        diaryRepository.insertProductDiaryEntry(
            productDiaryEntry = ProductDiaryEntry(
                weight = weight,
                mealName = mealName,
                utcTimestamp = currentTimestamp,
                date = date,
                userId = userId,
                nutritionValues = nutritionValues,
                productId = product.id,
                productName = product.name,
                productMeasurementUnit = product.measurementUnit,
                editedUtcTimestamp = currentTimestamp
            ),
            userId = userId
        )
    }
}