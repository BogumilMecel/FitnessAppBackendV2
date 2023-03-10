package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

data class DiaryUseCases(
    val getDiaryEntries: GetDiaryEntries,
    val insertProductDiaryEntryUseCase: InsertProductDiaryEntryUseCase,
    val deleteDiaryEntry: DeleteDiaryEntry,
    val getUserCaloriesSum: GetUserCaloriesSum
)
