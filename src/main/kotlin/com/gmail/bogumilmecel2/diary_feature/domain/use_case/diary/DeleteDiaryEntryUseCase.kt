package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeleteDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryEntryType
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase

class DeleteDiaryEntryUseCase(
    private val diaryRepository: DiaryRepository,
    private val isDateInValidRangeUseCaseUseCase: IsDateInValidRangeUseCaseUseCase
) {

    suspend operator fun invoke(
        deleteDiaryEntryRequest: DeleteDiaryEntryRequest,
        userId: String
    ): Resource<Unit> {
        return when (deleteDiaryEntryRequest.diaryEntryType) {
            DiaryEntryType.PRODUCT -> {
                val productDiaryEntry = diaryRepository.getProductDiaryEntry(id = deleteDiaryEntryRequest.diaryEntryId).data ?: return Resource.Error()

                if (productDiaryEntry.userId != userId) return Resource.Error()
                if (!isDateInValidRangeUseCaseUseCase(productDiaryEntry.creationDateTime.date)) return Resource.Error()

                // TODO: Revert changes when deleting is handled with device id
//                diaryRepository.deleteProductDiaryEntry(
//                    productDiaryEntryId = deleteDiaryEntryRequest.diaryEntryId,
//                    userId = userId
//                )
                diaryRepository.editProductDiaryEntry(
                    productDiaryEntry = productDiaryEntry.copy(
                        changeDateTime = CustomDateUtils.getUtcDateTime(),
                        deleted = true
                    ),
                )
            }
            DiaryEntryType.RECIPE -> {
                val recipeDiaryEntry = diaryRepository.getRecipeDiaryEntry(id = deleteDiaryEntryRequest.diaryEntryId).data ?: return Resource.Error()

                if (recipeDiaryEntry.userId != userId) return Resource.Error()
                if (!isDateInValidRangeUseCaseUseCase(recipeDiaryEntry.creationDateTime.date)) return Resource.Error()

                // TODO: Revert changes when deleting is handled with device id
//                diaryRepository.deleteRecipeDiaryEntry(
//                    recipeDiaryEntryId = deleteDiaryEntryRequest.diaryEntryId,
//                    userId = userId
//                )
                diaryRepository.editRecipeDiaryEntry(
                    recipeDiaryEntry = recipeDiaryEntry.copy(
                        changeDateTime = CustomDateUtils.getUtcDateTime(),
                        deleted = true
                    ),
                )
            }
        }
    }
}