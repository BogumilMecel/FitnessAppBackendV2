package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto

interface PriceRepository {
    suspend fun getPrice(productId: String, country: Country): Resource<PriceDto?>
    suspend fun addPrice(productId: String, price: PriceDto): Resource<Boolean>
    suspend fun updatePrice(newValue: Double, priceId: String): Resource<Boolean>
}