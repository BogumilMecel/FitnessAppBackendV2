package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository

import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.Price
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto

interface PriceRepository {
    suspend fun getPriceDto(productId: String, currency: Currency): Resource<PriceDto?>
    suspend fun getPrice(productId: String, currency: Currency): Resource<Price?>
    suspend fun addPrice(productId: String, price: Price): Resource<Boolean>
    suspend fun updatePrice(price: PriceDto): Resource<Boolean>
}