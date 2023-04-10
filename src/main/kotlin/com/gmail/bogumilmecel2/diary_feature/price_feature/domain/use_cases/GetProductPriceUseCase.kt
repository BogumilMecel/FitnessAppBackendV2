package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.ProductPriceResponse
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.toProductPrice
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository

class GetProductPriceUseCase(
    private val priceRepository: PriceRepository,
    private val getProductUseCase: GetProductUseCase
) {
    suspend operator fun invoke(productId: String, currency: Currency, country: Country): Resource<ProductPriceResponse> {
        return when(val productResource = getProductUseCase(productId = productId)) {
            is Resource.Error -> {
                Resource.Error()
            }

            is Resource.Success -> {
                productResource.data?.let { product ->
                    val priceResource = priceRepository.getPrice(
                        productId = productId,
                        country = country
                    )

                    when(priceResource) {
                        is Resource.Success -> {
                            Resource.Success(
                                data = ProductPriceResponse(
                                    productPrice = priceResource.data?.toProductPrice(
                                        nutritionValues = product.nutritionValues,
                                        currency = currency
                                    )
                                )
                            )
                        }

                        is Resource.Error -> {
                            Resource.Error()
                        }
                    }

                } ?: kotlin.run {
                    Resource.Error()
                }
            }
        }
    }
}