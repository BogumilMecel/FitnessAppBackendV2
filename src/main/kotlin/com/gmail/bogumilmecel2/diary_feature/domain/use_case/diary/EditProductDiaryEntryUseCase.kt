package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.EditProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.CalculateProductNutritionValuesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsTimestampInTwoWeeksUseCase

class EditProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductDiaryEntryUseCase: GetProductDiaryEntryUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val calculateProductNutritionValuesUseCase: CalculateProductNutritionValuesUseCase,
    private val isTimestampInTwoWeeksUseCase: IsTimestampInTwoWeeksUseCase
) {
    suspend operator fun invoke(
        editProductDiaryEntryRequest: EditProductDiaryEntryRequest,
        userId: String
    ): Resource<Unit> = with(editProductDiaryEntryRequest) {
        val productDiaryEntry = getProductDiaryEntryUseCase(productDiaryEntryId = productDiaryEntryId).data ?: return Resource.Error()

        if (productDiaryEntry.userId != userId) return Resource.Error()
        if (editProductDiaryEntryRequest.newWeight == productDiaryEntry.weight) return Resource.Error()
        if (!isTimestampInTwoWeeksUseCase(productDiaryEntry.utcTimestamp)) return Resource.Error()

        val product = getProductUseCase(productId = productDiaryEntry.productId).data ?: return Resource.Error()

        val newNutritionValues = calculateProductNutritionValuesUseCase(
            product = product,
            weight = newWeight
        ).data ?: return Resource.Error()

        return diaryRepository.editProductDiaryEntry(
            productDiaryEntry = productDiaryEntry.copy(
                nutritionValues = newNutritionValues,
                weight = newWeight
            ),
            userId = userId
        )
    }
}