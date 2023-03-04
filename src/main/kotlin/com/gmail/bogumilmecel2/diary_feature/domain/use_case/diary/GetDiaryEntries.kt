package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.toObject
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetDiaryEntries(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(
        date: String,
        userId: String
    ): Resource<List<DiaryItem>> {
        val allIDiaryItems = mutableListOf<DiaryItem>()
        val productDiaryEntries = diaryRepository.getProductDiaryEntries(
            date = date,
            userId = userId
        )
        val recipeDiaryEntries = diaryRepository.getRecipeDiaryEntries(
            date = date,
            userId
        )
        if (productDiaryEntries is Resource.Success) {
            productDiaryEntries.data.map { productDiaryEntryDto ->
                productDiaryEntryDto.toDiaryEntry()
            }.also {
                allIDiaryItems.addAll(it)
            }
        }
        if (recipeDiaryEntries is Resource.Success) {
            recipeDiaryEntries.data.map { recipeDiaryEntryDto ->
                recipeDiaryEntryDto.toObject()
            }.also {
                allIDiaryItems.addAll(it)
            }
        }
        return Resource.Success(data = allIDiaryItems)
    }
}