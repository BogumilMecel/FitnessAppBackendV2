package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductDtoUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetProductPriceUseCase

data class ProductUseCases(
    val getProductsUseCase: GetProductsUseCase,
    val getProductDtoUseCase: GetProductDtoUseCase,
    val getProductUseCase: GetProductUseCase,
    val insertProductUseCase: InsertProductUseCase,
    val addNewPriceUseCase: AddNewPriceUseCase,
    val getProductPriceUseCase: GetProductPriceUseCase,
    val postHistoryProductDiaryEntryUseCase: PostHistoryProductDiaryEntryUseCase
)
