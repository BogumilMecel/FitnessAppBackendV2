package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WeightEntry(
    @SerialName("value")
    val value: Double? = null,

    @SerialName("creation_date_time")
    val creationDateTime: LocalDateTime? = null,

    @SerialName("date")
    val date: LocalDate? = null
)

data class WeightEntryDto(
    @BsonId val _id: ObjectId,
    val userId: String,
    val value: Double,
    val creationDateTime: LocalDateTime,
    val date: LocalDate
)

fun WeightEntryDto.toWeightEntry(): WeightEntry = WeightEntry(
    value = value,
    creationDateTime = creationDateTime,
    date = date
)