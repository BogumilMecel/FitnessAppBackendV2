package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetUserProductDiaryEntriesUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(userId: String): Resource<List<ProductDiaryEntry>> {
        return diaryRepository.getProductDiaryEntries(userId = userId)
    }
}