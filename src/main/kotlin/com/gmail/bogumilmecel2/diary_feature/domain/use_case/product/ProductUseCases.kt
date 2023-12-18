package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetProductPriceUseCase

data class ProductUseCases(
    val getProductsUseCase: GetProductsUseCase,
    val insertProductUseCase: InsertProductUseCase,
    val searchForProductWithBarcode: SearchForProductWithBarcode,
    val addNewPriceUseCase: AddNewPriceUseCase,
    val getProductPriceUseCase: GetProductPriceUseCase
)
