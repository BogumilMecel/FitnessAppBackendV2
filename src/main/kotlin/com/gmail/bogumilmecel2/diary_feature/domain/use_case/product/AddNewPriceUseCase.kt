package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.domain.model.convertToDefaultCurrency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewPriceRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.ProductPrice
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.toProductPrice
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository
import org.bson.types.ObjectId

class AddNewPriceUseCase(
    private val priceRepository: PriceRepository,
    private val getProductUseCase: GetProductUseCase
) {

    suspend operator fun invoke(
        newPriceRequest: NewPriceRequest,
        productId: String,
        country: Country,
        currency: Currency
    ): Resource<ProductPrice> = with(newPriceRequest) {
        if (paidHowMuch <= 0 || paidForWeight <= 0) {
            return Resource.Error()
        } else {
            getProductUseCase(productId = productId).data?.let { product ->
                val priceResource = priceRepository.getPrice(
                    productId = productId,
                    country = country
                )
                when (priceResource) {
                    is Resource.Error -> {
                        return Resource.Error()
                    }

                    is Resource.Success -> {
                        val priceValueFor100g = calculatePriceValueFor100g(
                            value = paidHowMuch,
                            weight = paidForWeight
                        )

                        val priceFor100gInUSD = when (currency) {
                            Currency.USD -> priceValueFor100g
                            else -> priceValueFor100g.convertToDefaultCurrency(currency)
                        }

                        priceResource.data?.let { priceDto ->
                            val newValueInUSD = calculateValueBetweenTwoValues(
                                firstValue = priceDto.valueFor100gInUSD,
                                secondValue = priceFor100gInUSD
                            ).round(2)

                            val updatePriceResource = priceRepository.updatePrice(
                                newValue = newValueInUSD,
                                priceId = priceDto.productId
                            )

                            if (updatePriceResource is Resource.Success && updatePriceResource.data) {
                                return Resource.Success(
                                    data = priceDto.copy(valueFor100gInUSD = newValueInUSD).toProductPrice(
                                        nutritionValues = product.nutritionValues,
                                        currency = currency
                                    )
                                )
                            }

                        } ?: kotlin.run {
                            val newPriceDto = PriceDto(
                                _id = ObjectId(),
                                productId = product.id,
                                country = country,
                                valueFor100gInUSD = priceFor100gInUSD.round(2)
                            )

                            val addNewPriceResource = priceRepository.addPrice(
                                productId = product.id,
                                price = newPriceDto
                            )

                            if (addNewPriceResource is Resource.Success && addNewPriceResource.data) {
                                return Resource.Success(
                                    data = newPriceDto.toProductPrice(
                                        nutritionValues = product.nutritionValues,
                                        currency = currency
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        return Resource.Error()
    }

    private fun calculatePriceValueFor100g(value: Double, weight: Int) = value / weight.toDouble() * 100.0

    private fun calculateValueBetweenTwoValues(firstValue: Double, secondValue: Double) =
        (firstValue + secondValue) / 2.0
}