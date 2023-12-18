package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WeightEntry(
    val value: Double = 0.0,
    val utcTimestamp: Long = 0,
    val date: String = ""
)

data class WeightEntryDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val userId: String,
    val value: Double,
    val utcTimestamp: Long,
    val date: String
)

fun WeightEntry.toDto(userId: String): WeightEntryDto = WeightEntryDto(
    _id = ObjectId(),
    value = value,
    utcTimestamp = utcTimestamp,
    userId = userId,
    date = date
)

fun WeightEntryDto.toObject(): WeightEntry = WeightEntry(
    value = value,
    utcTimestamp = utcTimestamp,
    date = date
)