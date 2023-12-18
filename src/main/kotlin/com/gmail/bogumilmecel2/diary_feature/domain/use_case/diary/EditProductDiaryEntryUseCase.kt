package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.EditProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class EditProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(
        editProductDiaryEntryRequest: EditProductDiaryEntryRequest,
        userId: String
    ): Resource<Unit> = with(editProductDiaryEntryRequest) {
        if (userId != productDiaryEntry.userId) {
            return Resource.Error()
        }
        if (newWeight <= 0) {
            return Resource.Error()
        }

        val newNutritionValues = editProductDiaryEntryRequest.productDiaryEntry.product.calculateNutritionValues(
            weight = newWeight
        )
        val editResource = diaryRepository.editDiaryEntry(
            productDiaryEntry = productDiaryEntry.copy(
                nutritionValues = newNutritionValues,
                weight = newWeight
            ),
            userId = userId
        )

        return when (editResource) {
            is Resource.Success -> {
                if (editResource.data) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error()
                }
            }

            is Resource.Error -> {
                Resource.Error()
            }
        }
    }
}