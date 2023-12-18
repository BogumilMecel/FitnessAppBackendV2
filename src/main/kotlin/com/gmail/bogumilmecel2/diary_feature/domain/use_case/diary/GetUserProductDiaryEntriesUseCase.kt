package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils.getOrMinDateTime
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import kotlinx.datetime.LocalDateTime

class GetUserProductDiaryEntriesUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestDateTime: LocalDateTime?
    ): Resource<List<ProductDiaryEntry>> {
        return diaryRepository.getProductDiaryEntries(
            userId = userId,
            latestDateTime = latestDateTime.getOrMinDateTime()
        )
    }
}