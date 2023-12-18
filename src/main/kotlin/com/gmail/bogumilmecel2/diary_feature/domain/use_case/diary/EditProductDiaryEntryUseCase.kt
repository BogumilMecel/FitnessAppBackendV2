package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
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
        val originalProductDiaryEntry = getProductDiaryEntryUseCase(productDiaryEntryId = id).data ?: return Resource.Error()
        if (originalProductDiaryEntry.userId != userId) return Resource.Error()
        if (productDiaryEntry.weight == originalProductDiaryEntry.weight) return Resource.Error()

        val date = originalProductDiaryEntry.date ?: return Resource.Error()
        if (!isDateInValidRangeUseCaseUseCase(date)) return Resource.Error()

        val newProductDiaryEntry = productDiaryEntry.copy(
            changeDateTime = CustomDateUtils.getUtcDateTime()
        )

        val resource = diaryRepository.editProductDiaryEntry(
            productDiaryEntry = newProductDiaryEntry,
            userId = userId
        )

        return when(resource) {
            is Resource.Error -> Resource.Error()
            is Resource.Success -> Resource.Success(newProductDiaryEntry)
        }
    }
}