package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetPriceUseCase

class GetProductsUseCase(
    private val repository: DiaryRepository,
    private val getPrice: GetPriceUseCase
) {

    suspend operator fun invoke(
        searchText:String,
        currency: Currency,
        country: Country
    ):Resource<List<Product>>{
        return when(val productsResource = repository.getProducts(text = searchText)) {
            is Resource.Success -> {
                val products = productsResource.data
                val productWithPrices = products.map {
                    it.copy(
                        price = getPrice(
                            productId = it.id,
                            currency = currency
                        ).data
                    )
                }
                Resource.Success(productWithPrices)
            }
            is Resource.Error -> {
                productsResource
            }
        }
    }
}