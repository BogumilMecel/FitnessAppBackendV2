package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetUserProductDiaryEntriesUseCase

data class UserDataUseCases(
    val saveNutritionValuesUseCase: SaveNutritionValuesUseCase,
    val handleUserInformationUseCase: HandleUserInformationUseCase,
    val getUserProductDiaryEntriesUseCase: GetUserProductDiaryEntriesUseCase
)
