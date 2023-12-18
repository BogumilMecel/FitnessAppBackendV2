package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeleteDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryEntryType
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class DeleteDiaryEntry(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        deleteDiaryEntryRequest: DeleteDiaryEntryRequest,
        userId:String
    ):Resource<Unit>{
        return when(deleteDiaryEntryRequest.diaryEntryType) {
            DiaryEntryType.PRODUCT -> {
                diaryRepository.deleteProductDiaryEntry(
                    productDiaryEntryId = deleteDiaryEntryRequest.diaryEntryId,
                    userId = userId
                )
            }
            DiaryEntryType.RECIPE -> {
                diaryRepository.deleteRecipeDiaryEntry(
                    recipeDiaryEntryId = deleteDiaryEntryRequest.diaryEntryId,
                    userId = userId
                )
            }
        }
    }
}