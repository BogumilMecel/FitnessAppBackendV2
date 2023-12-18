package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WeightEntry(
    val value: Double = 0.0,
    val creationDateTime: LocalDateTime? = null,
    val date: LocalDate? = null
)

data class WeightEntryDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val userId: String,
    val value: Double,
    val creationDateTime: LocalDateTime? = null,
    val date: LocalDate? = null
)

fun WeightEntry.toDto(userId: String): WeightEntryDto = WeightEntryDto(
    _id = ObjectId(),
    value = value,
    creationDateTime = creationDateTime,
    userId = userId,
    date = date
)

fun WeightEntryDto.toObject(): WeightEntry = WeightEntry(
    value = value,
    creationDateTime = creationDateTime,
    date = date
)