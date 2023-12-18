package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model

import com.gmail.bogumilmecel2.common.domain.model.Currency
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Price(
    val valueFor100Calories: Double,
    val valueFor100Carbohydrates: Double,
    val valueFor10Protein: Double,
    val valueFor10Fat: Double,
    val currency: Currency = Currency.PLN
)

data class PriceDto(
    @BsonId val _id: ObjectId,
    val valueFor100Calories: Double,
    val valueFor100Carbohydrates: Double,
    val valueFor10Protein: Double,
    val valueFor10Fat: Double,
    val currency: Currency = Currency.PLN,
    val productId: String
)

fun Price.toDto(productId: String) = PriceDto(
    _id = ObjectId(),
    valueFor100Calories = valueFor100Calories,
    valueFor100Carbohydrates = valueFor100Carbohydrates,
    valueFor10Protein = valueFor10Protein,
    valueFor10Fat = valueFor10Fat,
    currency = currency,
    productId = productId
)

fun PriceDto.toPrice() = Price(
    valueFor100Calories = valueFor100Calories,
    valueFor100Carbohydrates = valueFor100Carbohydrates,
    valueFor10Protein = valueFor10Protein,
    valueFor10Fat = valueFor10Fat,
    currency = currency,
)