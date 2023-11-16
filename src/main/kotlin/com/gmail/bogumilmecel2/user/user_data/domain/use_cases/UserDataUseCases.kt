package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetUserProductDiaryEntriesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetUserRecipeDiaryEntriesUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetUserProductsUseCase

data class UserDataUseCases(
    val saveNutritionValuesUseCase: SaveNutritionValuesUseCase,
    val handleUserInformationUseCase: HandleUserInformationUseCase,
    val getUserProductDiaryEntriesUseCase: GetUserProductDiaryEntriesUseCase,
    val getUserRecipeDiaryEntriesUseCase: GetUserRecipeDiaryEntriesUseCase,
    val getUserProductsUseCase: GetUserProductsUseCase
)
