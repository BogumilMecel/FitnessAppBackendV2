package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.constants.Constants.DEFAULT_PAGE_SIZE
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.CalculateSkipUseCase

class GetProductsUseCase(
    private val repository: DiaryRepository,
    private val calculateSkipUseCase: CalculateSkipUseCase
) {

    suspend operator fun invoke(
        searchText: String,
        currency: Currency,
        country: Country,
        page: Int
    ): Resource<List<Product>> {
        val resource = repository.getProducts(
            text = searchText,
            skip = calculateSkipUseCase(
                page = page,
                sizePerPage = DEFAULT_PAGE_SIZE
            )
        )

        return when (resource) {
            is Resource.Success -> {
                Resource.Success(resource.data)
            }

            is Resource.Error -> {
                resource
            }
        }
    }
}