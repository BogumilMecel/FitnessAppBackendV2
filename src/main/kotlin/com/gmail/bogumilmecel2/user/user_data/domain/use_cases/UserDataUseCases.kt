package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetUserDiaryItemsUseCase

data class UserDataUseCases(
    val saveNutritionValuesUseCase: SaveNutritionValuesUseCase,
    val handleUserInformationUseCase: HandleUserInformationUseCase,
    val getUserDiaryItemsUseCase: GetUserDiaryItemsUseCase
)
