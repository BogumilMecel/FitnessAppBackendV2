package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductDiaryHistoryUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        pageStringValue: String,
        searchText: String?
    ): Resource<List<ProductDiaryHistoryItem>> {
        val page = pageStringValue.toIntOrNull() ?: return Resource.Error()

        if (page < 1) return Resource.Error()

        val skip = (Constants.DEFAULT_PAGE_SIZE * page) - 20
        val limit = Constants.DEFAULT_PAGE_SIZE * page

        return diaryRepository.getProductDiaryHistory(
            userId = userId,
            skip = skip,
            limit = limit,
            searchText = searchText,
        )
    }
}