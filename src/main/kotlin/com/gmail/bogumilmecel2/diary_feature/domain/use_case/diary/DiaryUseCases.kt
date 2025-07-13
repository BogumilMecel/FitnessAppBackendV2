package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.EditRecipeDiaryEntryUseCase

data class DiaryUseCases(
    val getDiaryEntriesUseCase: GetDiaryEntriesUseCase,
    val insertProductDiaryEntryUseCase: InsertProductDiaryEntryUseCase,
    val deleteDiaryEntryUseCase: DeleteDiaryEntryUseCase,
    val editProductDiaryEntryUseCase: EditProductDiaryEntryUseCase,
    val editRecipeDiaryEntryUseCase: EditRecipeDiaryEntryUseCase,
    val getProductDiaryHistoryUseCase: GetProductDiaryHistoryUseCase,
    val getHistoryProductDiaryEntriesUseCase: GetHistoryProductDiaryEntriesUseCase,
)
