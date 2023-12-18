package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeleteDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryEntryType
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsTimestampInTwoWeeksUseCase

class DeleteDiaryEntry(
    private val diaryRepository: DiaryRepository,
    private val isTimestampInTwoWeeksUseCase: IsTimestampInTwoWeeksUseCase
) {

    suspend operator fun invoke(
        deleteDiaryEntryRequest: DeleteDiaryEntryRequest,
        userId: String
    ): Resource<Boolean> {
        return when (deleteDiaryEntryRequest.diaryEntryType) {
            DiaryEntryType.PRODUCT -> {
                val productDiaryEntry = diaryRepository.getProductDiaryEntry(id = deleteDiaryEntryRequest.diaryEntryId).data ?: return Resource.Error()

                if (!isTimestampInTwoWeeksUseCase(productDiaryEntry.utcTimestamp)) return Resource.Error()

                diaryRepository.deleteProductDiaryEntry(
                    productDiaryEntryId = deleteDiaryEntryRequest.diaryEntryId,
                    userId = userId
                )
            }
            DiaryEntryType.RECIPE -> {
                val recipeDiaryEntry = diaryRepository.getRecipeDiaryEntry(id = deleteDiaryEntryRequest.diaryEntryId).data ?: return Resource.Error()

                if (!isTimestampInTwoWeeksUseCase(recipeDiaryEntry.utcTimestamp)) return Resource.Error()

                diaryRepository.deleteRecipeDiaryEntry(
                    recipeDiaryEntryId = deleteDiaryEntryRequest.diaryEntryId,
                    userId = userId
                )
            }
        }
    }
}