package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class InsertDiaryEntry(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        productDiaryEntryPostRequest: ProductDiaryEntryPostRequest,
        userId: String
    ): Resource<ProductDiaryEntryDto> {
        return if (productDiaryEntryPostRequest.weight <= 0) {
            Resource.Error()
        } else if (productDiaryEntryPostRequest.mealName.isEmpty() || productDiaryEntryPostRequest.date.isEmpty()) {
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