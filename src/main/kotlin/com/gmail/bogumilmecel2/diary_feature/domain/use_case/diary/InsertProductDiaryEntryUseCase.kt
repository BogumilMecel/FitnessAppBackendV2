package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.DateUtils.isValidDate
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class InsertProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntry> {
        return if (productDiaryEntryPostRequest.weight <= 0) {
            Resource.Error()
        } else if (productDiaryEntryPostRequest.date.isEmpty()) {
            Resource.Error()
        } else if(!productDiaryEntryPostRequest.date.isValidDate()) {
            Resource.Error()
        } else {
            diaryRepository.insertDiaryEntry(
                productDiaryEntry = ProductDiaryEntry(
                    product = productDiaryEntryPostRequest.product,
                    weight = productDiaryEntryPostRequest.weight,
                    mealName = productDiaryEntryPostRequest.mealName,
                    timestamp = productDiaryEntryPostRequest.timestamp,
                    date = productDiaryEntryPostRequest.date,
                    userId = userId,
                    nutritionValues = productDiaryEntryPostRequest.product.calculateNutritionValues(weight = productDiaryEntryPostRequest.weight)
                ),
                userId = userId
            )
        }
    }
}