package com.gmail.bogumilmecel2.diary_feature.price_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class PriceRepositoryImp(
    private val priceCol: CoroutineCollection<PriceDto>
): PriceRepository, BaseRepository() {

    override suspend fun getPrice(productId: String, country: Country): Resource<PriceDto?> {
        return handleRequest {
            priceCol.findOne(PriceDto::productId eq productId, PriceDto::country eq country)
        }
    }


    override suspend fun addPrice(productId: String, price: PriceDto): Resource<Boolean> {
        return handleRequest {
            priceCol.insertOne(price).wasAcknowledged()
        }
    }

    override suspend fun updatePrice(newValue: Double, priceId: String): Resource<Boolean> {
        return handleRequest {
           priceCol.updateOneById(
               id = PriceDto::_id eq priceId.toObjectId(),
               update = set(PriceDto::valueFor100gInUSD setTo newValue)
           ).wasAcknowledged()
        }
    }
}