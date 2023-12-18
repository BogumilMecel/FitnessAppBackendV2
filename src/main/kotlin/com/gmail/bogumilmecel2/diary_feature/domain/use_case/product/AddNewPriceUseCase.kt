package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewPriceRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.Price
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.toPrice
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository

class AddNewPriceUseCase(
    private val priceRepository: PriceRepository,
    private val getProductUseCase: GetProductUseCase
) {

    suspend operator fun invoke(
        newPriceRequest: NewPriceRequest,
        productId: String
    ): Resource<Price> = with(newPriceRequest) {
        if (paidHowMuch <= 0 || paidForWeight <= 0) {
            return Resource.Error()
        } else {
            getProductUseCase(productId = productId).data?.let { product ->
                val priceWithRequestCurrencyResource = priceRepository.getPriceDto(
                    productId = productId,
                    currency = currency
                )
                when (priceWithRequestCurrencyResource) {
                    is Resource.Error -> {
                        return Resource.Error()
                    }

                    is Resource.Success -> {
                        with(newPriceRequest) {
                            val priceValueFor100g = calculatePriceValueFor100g(
                                value = paidHowMuch,
                                weight = paidForWeight
                            )
                            val newPrice = calculatePrice(
                                value = priceValueFor100g,
                                nutritionValues = product.nutritionValues,
                                currency = currency
                            )
                            priceWithRequestCurrencyResource.data?.let {
                                val calculatedPrice = calculatePriceBetweenTwoPrices(
                                    firstPrice = it.toPrice(),
                                    secondPrice = newPrice
                                )

                                val updatePriceResource = priceRepository.updatePrice(
                                    price = it.copy(
                                        valueFor100Calories = calculatedPrice.valueFor100Calories,
                                        valueFor100Carbohydrates = calculatedPrice.valueFor100Carbohydrates,
                                        valueFor10Protein = calculatedPrice.valueFor10Protein,
                                        valueFor10Fat = calculatedPrice.valueFor10Fat,
                                    )
                                )

                                return if (updatePriceResource is Resource.Success && updatePriceResource.data) {
                                    Resource.Success(data = calculatedPrice)
                                } else Resource.Error()

                            } ?: kotlin.run {
                                val addNewPriceResource = priceRepository.addPrice(
                                    productId = product.id,
                                    price = newPrice
                                )

                                if (addNewPriceResource is Resource.Success && addNewPriceResource.data) {
                                    Resource.Success(data = newPrice)
                                } else Resource.Error()
                            }
                        }
                    }
                }
            }
        }
        return Resource.Error()
    }

    private fun calculatePrice(value: Double, nutritionValues: NutritionValues, currency: Currency) = Price(
        valueFor100Calories = (value / nutritionValues.calories.toDouble() * 100.0).round(2),
        valueFor100Carbohydrates = (value / nutritionValues.carbohydrates * 100.0).round(2),
        valueFor10Protein = (value / nutritionValues.protein * 10.0).round(2),
        valueFor10Fat = (value / nutritionValues.fat * 10.0).round(2),
        currency = currency
    )

    private fun calculatePriceValueFor100g(value: Double, weight: Int) = value / weight.toDouble() * 100.0

    private fun calculatePriceBetweenTwoPrices(firstPrice: Price, secondPrice: Price) = Price(
        valueFor100Calories = (firstPrice.valueFor100Calories + secondPrice.valueFor100Calories) / 2.0,
        valueFor100Carbohydrates = (firstPrice.valueFor100Carbohydrates + secondPrice.valueFor100Carbohydrates) / 2.0,
        valueFor10Protein = (firstPrice.valueFor10Protein + secondPrice.valueFor10Protein) / 2.0,
        valueFor10Fat = (firstPrice.valueFor10Fat + secondPrice.valueFor10Fat) / 2.0,
        currency = firstPrice.currency
    )
}