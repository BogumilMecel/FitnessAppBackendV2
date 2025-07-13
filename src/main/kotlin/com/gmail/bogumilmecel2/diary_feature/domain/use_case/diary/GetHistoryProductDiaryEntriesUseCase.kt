package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.domain.model.exceptions.ForbiddenException
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.getOrMinDateTime
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.HistoryProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import kotlinx.datetime.LocalDateTime

class GetHistoryProductDiaryEntriesUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String?,
        latestDateTime: LocalDateTime?
    ): Resource<List<HistoryProductDiaryEntry>> {
        if (userId == null) throw ForbiddenException

        return diaryRepository.getHistoryProductDiaryEntries(
            userId = userId,
            latestDateTime = latestDateTime.getOrMinDateTime()
        )
    }
}