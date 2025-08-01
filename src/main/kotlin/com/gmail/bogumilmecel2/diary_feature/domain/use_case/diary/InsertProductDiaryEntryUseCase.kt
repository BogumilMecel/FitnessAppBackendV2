package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.NewProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.HistoryProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.PostHistoryProductDiaryEntryUseCase
import org.bson.types.ObjectId

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase,
    private val postHistoryProductDiaryEntryUseCase: PostHistoryProductDiaryEntryUseCase
) {
    suspend operator fun invoke(
        newProductDiaryEntryRequest: NewProductDiaryEntryRequest,
        userId: String
    ): Resource<HistoryProductDiaryEntry?> = with(newProductDiaryEntryRequest) {
        if (weight <= 0) return Resource.Error(InvalidWeightException)
        val product = diaryRepository.getProduct(productId = productId).data ?: return Resource.Error(ProductNotFoundException)
        if (!isDateInValidRangeUseCaseUseCase(date)) return Resource.Error(InvalidDateException)

        val currentDate = CustomDateUtils.getUtcDateTime()

        val insertResource = diaryRepository.insertProductDiaryEntry(
            productDiaryEntry = ProductDiaryEntryDto(
                _id = ObjectId(),
                weight = weight,
                mealName = mealName,
                date = date.toString(),
                userId = userId,
                nutritionValues = nutritionValues,
                productId = productId,
                productName = product.name,
                measurementUnit = product.measurementUnit,
                creationDateTime = currentDate,
                changeDateTime = currentDate,
                deleted = false
            )
        )

        return when(insertResource) {
            is Resource.Error -> Resource.Error()
            is Resource.Success -> {
                Resource.Success(
                    data = runCatching {
                        postHistoryProductDiaryEntryUseCase(productDiaryEntry = insertResource.data)
                    }.getOrNull()
                )
            }
        }
    }
}