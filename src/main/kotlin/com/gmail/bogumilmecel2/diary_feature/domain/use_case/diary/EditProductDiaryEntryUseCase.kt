package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.EditProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateProductNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase

class EditProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductDiaryEntryUseCase: GetProductDiaryEntryUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val calculateProductNutritionValuesUseCase: CalculateProductNutritionValuesUseCase
) {
    suspend operator fun invoke(
        editProductDiaryEntryRequest: EditProductDiaryEntryRequest,
        userId: String
    ): Resource<Unit> = with(editProductDiaryEntryRequest) {
        val productDiaryEntry = getProductDiaryEntryUseCase(productDiaryEntryId = productDiaryEntryId).data ?: return Resource.Error()
        val product = getProductUseCase(productId = productDiaryEntry.productId).data ?: return Resource.Error()
        val newNutritionValues = calculateProductNutritionValuesUseCase(
            product = product,
            weight = newWeight
        ).data ?: return Resource.Error()

        return diaryRepository.editProductDiaryEntry(
            userId = userId,
            productDiaryEntryId = productDiaryEntryId,
            newWeight = newWeight,
            newNutritionValues = newNutritionValues
        )
    }
}