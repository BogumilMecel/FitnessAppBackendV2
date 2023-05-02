package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.EditProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.calculateNutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class EditProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductDiaryEntryUseCase: GetProductDiaryEntryUseCase
) {
    suspend operator fun invoke(
        editProductDiaryEntryRequest: EditProductDiaryEntryRequest,
        userId: String
    ): Resource<Unit> = with(editProductDiaryEntryRequest) {
        if (newWeight <= 0) {
            return Resource.Error()
        }

        val productDiaryEntryResource = getProductDiaryEntryUseCase(
            productDiaryEntryId = productDiaryEntryId
        )

        if (productDiaryEntryResource !is Resource.Success) {
            return Resource.Error()
        }

        val productDiaryEntry = productDiaryEntryResource.data

        val newNutritionValues = productDiaryEntry.product.calculateNutritionValues(
            weight = newWeight
        )

        val wasAcknowledged = diaryRepository.editProductDiaryEntry(
            productDiaryEntry = productDiaryEntry.copy(
                nutritionValues = newNutritionValues,
                weight = newWeight
            ),
            userId = userId
        ).data ?: return Resource.Error()

        if (!wasAcknowledged) {
            return Resource.Error()
        }

        return Resource.Success(Unit)
    }
}