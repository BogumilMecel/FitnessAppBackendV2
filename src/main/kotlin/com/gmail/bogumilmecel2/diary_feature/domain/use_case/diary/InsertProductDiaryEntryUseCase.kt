package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import org.bson.types.ObjectId

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntryPostRequest) {
        productId ?: return Resource.Error()
        date ?: return Resource.Error()
        weight ?: return Resource.Error()
        mealName ?: return Resource.Error()
        nutritionValues ?: return Resource.Error()

        val product = diaryRepository.getProduct(productId = productId).data ?: return Resource.Error()

        if (weight <= 0) return Resource.Error()
        if (!isDateInValidRangeUseCaseUseCase(date)) return Resource.Error()

        val currentDate = CustomDateUtils.getUtcDateTime()

        diaryRepository.insertProductDiaryEntry(
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
            ),
            userId = userId
        )
    }
}