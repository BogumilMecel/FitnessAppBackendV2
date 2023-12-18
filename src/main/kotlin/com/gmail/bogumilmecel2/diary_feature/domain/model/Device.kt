package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Device(
    val id: String = "",
    val creationDate: LocalDateTime,
    val lastLoggedDate: LocalDateTime,
)

data class DeviceDto(
    @BsonId val _id: ObjectId,
    val userId: String,
    val creationDate: LocalDateTime,
    val lastLoggedInDate: LocalDateTime,
)

fun Device.toDto(userId: String) = DeviceDto(
    _id = id.toObjectId(),
    userId = userId,
    creationDate = creationDate,
    lastLoggedInDate = lastLoggedDate
)

fun DeviceDto.toDevice() = Device(
    id = _id.toString(),
    creationDate = creationDate,
    lastLoggedDate = lastLoggedInDate
)