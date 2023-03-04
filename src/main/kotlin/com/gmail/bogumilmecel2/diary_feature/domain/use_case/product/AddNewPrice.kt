package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.price.Price
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewPriceRequest
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase

class AddNewPrice(
    private val diaryRepository: DiaryRepository,
    private val getProductUseCase: GetProductUseCase
) {

    suspend operator fun invoke(
        newPriceRequest: NewPriceRequest,
        productId: String
    ):Resource<Price> = with(newPriceRequest){
        return@with if (paidHowMuch <= 0 || paidForWeight <= 0) {
            Resource.Error()
        } else {
            getProductUseCase(
                productId = productId
            ).data?.let { product ->
                val calculatedPrice = calculatePriceFor100g(
                    value = paidHowMuch,
                    weight = paidForWeight
                ).let {
                    Price(
                        currency = newPriceRequest.currency,
                        value = it
                    )
                }

                with(product.price) {
                    val newPrice = this?.copy(value = (this.value + calculatedPrice.value) / 2.0) ?: calculatedPrice

                    diaryRepository.addNewPrice(
                        productId = productId,
                        price = newPrice
                    )
                }
            } ?: Resource.Error()
        }
    }

    private fun calculatePriceFor100g(value: Double, weight: Int) = value/weight.toDouble()*100.0
}