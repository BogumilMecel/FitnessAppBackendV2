package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateProductNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import kotlinx.datetime.LocalDate

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

        if (weight <= 0) return Resource.Error()
        if (date.isEmpty()) return Resource.Error()
        if (!date.isValidDate()) return Resource.Error()
        if (CustomDateUtils.getDaysFromNow(from = LocalDate.parse(date)) > Constants.Diary.MAXIMUM_MODIFY_DATE) return Resource.Error()

        val currentTimestamp = CustomDateUtils.getCurrentUtcTimestamp()

        diaryRepository.insertProductDiaryEntry(
            productDiaryEntry = ProductDiaryEntry(
                weight = productDiaryEntryPostRequest.weight,
                mealName = productDiaryEntryPostRequest.mealName,
                utcTimestamp = currentTimestamp,
                date = productDiaryEntryPostRequest.date,
                userId = userId,
                nutritionValues = calculateProductNutritionValuesUseCase(
                    product = product,
                    weight = productDiaryEntryPostRequest.weight
                ).data ?: return Resource.Error(),
                productId = productDiaryEntryPostRequest.productId,
                productName = product.name,
                productMeasurementUnit = product.measurementUnit,
                lastEditedUtcTimestamp = currentTimestamp
            ),
            userId = userId
        )
    }
}