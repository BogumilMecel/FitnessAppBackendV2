package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.domain.model.exceptions.*
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase

class EditProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductDiaryEntryUseCase: GetProductDiaryEntryUseCase,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {
    suspend operator fun invoke(
        productDiaryEntry: ProductDiaryEntry,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntry) {
        id ?: return Resource.Error(exception = InvalidIdException)
        nutritionValues ?: return Resource.Error(exception = InvalidNutritionValuesException)
        weight ?: return Resource.Error(exception = InvalidWeightException)

        if (weight <= 0) return Resource.Error(InvalidWeightException)
        val originalProductDiaryEntry = getProductDiaryEntryUseCase(productDiaryEntryId = id).data ?: return Resource.Error(exception = DiaryEntryNotFoundException)
        if (originalProductDiaryEntry.userId != userId) return Resource.Error(exception = ForbiddenException)
        if (weight == originalProductDiaryEntry.weight) return Resource.Error(InvalidWeightException)
        if (!isDateInValidRangeUseCaseUseCase(originalProductDiaryEntry.creationDateTime.date)) return Resource.Error(InvalidDateException)

        val newProductDiaryEntry = originalProductDiaryEntry.copy(
            changeDateTime = CustomDateUtils.getUtcDateTime(),
            nutritionValues = nutritionValues,
            weight = weight,
        )

        return when(diaryRepository.editProductDiaryEntry(productDiaryEntry = newProductDiaryEntry)) {
            is Resource.Error -> Resource.Error()
            is Resource.Success -> Resource.Success(newProductDiaryEntry.toProductDiaryEntry())
        }
    }
}