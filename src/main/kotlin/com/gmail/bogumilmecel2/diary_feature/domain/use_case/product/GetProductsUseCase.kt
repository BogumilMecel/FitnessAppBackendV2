package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductsUseCase(
    private val repository: DiaryRepository,
) {

    suspend operator fun invoke(
        searchText:String,
        currency: Currency,
        country: Country
    ):Resource<List<Product>>{
        return when(val productsResource = repository.getProducts(text = searchText)) {
            is Resource.Success -> {
                Resource.Success(productsResource.data)
            }
            is Resource.Error -> {
                productsResource
            }
        }
    }
}