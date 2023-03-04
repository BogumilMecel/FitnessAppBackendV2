package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WeightEntry(
    val value: Double,
    val timestamp: Long
)

data class WeightEntryDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val userId: String,
    val value: Double,
    val timestamp: Long
)

fun WeightEntry.toDto(userId: String): WeightEntryDto = WeightEntryDto(
    _id = ObjectId(),
    value = value,
    timestamp = timestamp,
    userId = userId
)

fun WeightEntryDto.toObject(): WeightEntry = WeightEntry(
    value = value,
    timestamp = timestamp
)