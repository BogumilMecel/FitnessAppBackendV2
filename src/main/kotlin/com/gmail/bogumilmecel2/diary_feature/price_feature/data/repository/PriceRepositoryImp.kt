package com.gmail.bogumilmecel2.diary_feature.price_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull

class PriceRepositoryImp(
    private val priceCol: MongoCollection<PriceDto>
) : PriceRepository, BaseRepository() {

    override suspend fun getPrice(productId: String, country: Country): Resource<PriceDto?> {
        return handleRequest {
            priceCol
                .find(
                    and(
                        eq(PriceDto::productId.name, productId),
                        eq(PriceDto::country.name, country)
                    )
                ).firstOrNull()
        }
    }


    override suspend fun addPrice(productId: String, price: PriceDto): Resource<Boolean> {
        return handleRequest {
            priceCol.insertOne(price).wasAcknowledged()
        }
    }

    override suspend fun updatePrice(newValue: Double, priceId: String): Resource<Boolean> {
        return handleRequest {
            priceCol.updateOne(
                filter = eq(PriceDto::_id.name, priceId.toObjectId()),
                update = Updates.set(PriceDto::valueFor100gInUSD.name, newValue)
            ).wasAcknowledged()
        }
    }
}