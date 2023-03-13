package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

data class ProductUseCases(
    val getProducts: GetProducts,
    val insertProductUseCase: InsertProductUseCase,
    val getProductHistoryUseCase: GetProductHistoryUseCase,
    val searchForProductWithBarcode: SearchForProductWithBarcode,
    val addNewPrice: AddNewPrice
)
