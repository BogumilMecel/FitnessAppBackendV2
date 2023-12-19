package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductDiaryEntryUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(productDiaryEntryId: String): Resource<ProductDiaryEntryDto?> {
        if (productDiaryEntryId.isEmpty()) {
            return Resource.Error()
        }

        return diaryRepository.getProductDiaryEntry(id = productDiaryEntryId)
    }
}