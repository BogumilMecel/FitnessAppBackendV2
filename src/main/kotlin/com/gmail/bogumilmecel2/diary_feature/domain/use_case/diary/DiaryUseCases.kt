package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.EditRecipeDiaryEntryUseCase

data class DiaryUseCases(
    val getDiaryEntries: GetDiaryEntries,
    val insertProductDiaryEntryUseCase: InsertProductDiaryEntryUseCase,
    val deleteDiaryEntry: DeleteDiaryEntry,
    val editProductDiaryEntryUseCase: EditProductDiaryEntryUseCase,
    val editRecipeDiaryEntryUseCase: EditRecipeDiaryEntryUseCase,
    val getProductDiaryHistoryUseCase: GetProductDiaryHistoryUseCase,
)
