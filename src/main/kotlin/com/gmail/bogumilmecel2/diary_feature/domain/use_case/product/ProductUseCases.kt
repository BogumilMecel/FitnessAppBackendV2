package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

data class ProductUseCases(
    val getProductsUseCase: GetProductsUseCase,
    val insertProductUseCase: InsertProductUseCase,
    val searchForProductWithBarcode: SearchForProductWithBarcode,
    val addNewPriceUseCase: AddNewPriceUseCase
)
