package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsTimestampInTwoWeeksUseCase

class EditProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val getProductDiaryEntryUseCase: GetProductDiaryEntryUseCase,
    private val isTimestampInTwoWeeksUseCase: IsTimestampInTwoWeeksUseCase
) {
    suspend operator fun invoke(
        productDiaryEntry: ProductDiaryEntry,
        userId: String
    ): Resource<ProductDiaryEntry> = with(productDiaryEntry) {
        val originalProductDiaryEntry = getProductDiaryEntryUseCase(
            productDiaryEntryId = id
        ).data ?: return Resource.Error()

        if (originalProductDiaryEntry.userId != userId) return Resource.Error()
        if (productDiaryEntry.weight == originalProductDiaryEntry.weight) return Resource.Error()
        if (!isTimestampInTwoWeeksUseCase(originalProductDiaryEntry.utcTimestamp)) return Resource.Error()

        val newProductDiaryEntry = productDiaryEntry.copy(lastEditedUtcTimestamp = CustomDateUtils.getCurrentUtcTimestamp())

        val resource = diaryRepository.editProductDiaryEntry(
            productDiaryEntry = newProductDiaryEntry,
            userId = userId
        )

        return when(resource) {
            is Resource.Error -> {
                Resource.Error()
            }

            is Resource.Success -> {
                Resource.Success(newProductDiaryEntry)
            }
        }
    }
}