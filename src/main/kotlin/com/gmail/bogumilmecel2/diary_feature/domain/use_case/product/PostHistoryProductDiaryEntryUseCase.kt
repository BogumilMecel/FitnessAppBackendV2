package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.HistoryProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.HistoryProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import org.bson.types.ObjectId

class PostHistoryProductDiaryEntryUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(productDiaryEntry: ProductDiaryEntryDto): HistoryProductDiaryEntry {
        val historyProductDiaryEntryDto = HistoryProductDiaryEntryDto(
            _id = ObjectId(),
            name = productDiaryEntry.productName,
            weight = productDiaryEntry.weight,
            measurementUnit = productDiaryEntry.measurementUnit,
            nutritionValues = productDiaryEntry.nutritionValues,
            userId = productDiaryEntry.userId,
            productId = productDiaryEntry.productId,
            latestDateTime = productDiaryEntry.changeDateTime,
        )

        return diaryRepository.insertHistoryProductDiaryEntry(historyProductDiaryEntryDto)
    }
} 