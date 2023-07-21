package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiarySearchItem
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductDiaryHistoryUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(userId: String): Resource<List<ProductDiarySearchItem>> {
        return diaryRepository.getProductDiaryHistory(userId = userId)
    }
}