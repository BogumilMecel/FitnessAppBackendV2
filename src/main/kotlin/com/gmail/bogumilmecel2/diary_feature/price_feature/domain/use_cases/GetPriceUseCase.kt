package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases

import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.Price
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository

class GetPriceUseCase(
    private val priceRepository: PriceRepository
) {
    suspend operator fun invoke(productId: String, currency: Currency): Resource<Price?> {
        return priceRepository.getPrice(productId = productId, currency = currency)
    }
}