package com.gmail.bogumilmecel2.diary_feature.price_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.Price
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.toDto
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.toPrice
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class PriceRepositoryImp(
    private val priceCol: CoroutineCollection<PriceDto>
): PriceRepository, BaseRepository() {

    override suspend fun getPriceDto(productId: String, currency: Currency): Resource<PriceDto?> {
        return handleRequest {
            priceCol.findOne(PriceDto::productId eq productId, PriceDto::currency eq currency)
        }
    }

    override suspend fun getPrice(productId: String, currency: Currency): Resource<Price?> {
        return handleRequest {
            priceCol.findOne(PriceDto::productId eq productId, PriceDto::currency eq currency)?.toPrice()
        }
    }

    override suspend fun addPrice(productId: String, price: Price): Resource<Boolean> {
        return handleRequest {
            priceCol.insertOne(price.toDto(productId = productId)).wasAcknowledged()
        }
    }

    override suspend fun updatePrice(price: PriceDto): Resource<Boolean> {
        return handleRequest {
           priceCol.updateOneById(
               id = PriceDto::_id eq price._id,
               update = set(
                   PriceDto::valueFor100Calories setTo price.valueFor100Calories,
                   PriceDto::valueFor100Carbohydrates setTo price.valueFor100Carbohydrates,
                   PriceDto::valueFor10Protein setTo price.valueFor10Protein,
                   PriceDto::valueFor10Fat setTo price.valueFor10Fat,
               )
           ).wasAcknowledged()
        }
    }
}