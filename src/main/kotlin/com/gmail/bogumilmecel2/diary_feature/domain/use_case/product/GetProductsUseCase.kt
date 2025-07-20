package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.constants.Constants.DEFAULT_PAGE_SIZE
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.copyType
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductsResponse
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
    ): Resource<ProductsResponse> {
        val resource = repository.getProducts(
            text = searchText,
            skip = calculateSkipUseCase(
                page = page,
                sizePerPage = DEFAULT_PAGE_SIZE
            )
        )

        return when (resource) {
            is Resource.Success -> {
                Resource.Success(
                    data = ProductsResponse(
                        results = resource.data,
                        page = page,
                        hasNextPage = resource.data.size % DEFAULT_PAGE_SIZE == 0
                    )
                )
            }

            is Resource.Error -> {
                resource.copyType()
            }
        }
    }
}