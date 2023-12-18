package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(productDiaryEntryId: String): Resource<ProductDiaryEntry> {
        if (productDiaryEntryId.isEmpty()) {
            return Resource.Error()
        }

        val productDiaryEntry = diaryRepository.getProductDiaryEntry(
            id = productDiaryEntryId
        ).data ?: return Resource.Error()

        return Resource.Success(
            data = productDiaryEntry
        )
    }
}