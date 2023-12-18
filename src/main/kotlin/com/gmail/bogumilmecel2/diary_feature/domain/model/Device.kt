package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Device(
    val id: String = "",
    val createdUtcTimestamp: Long,
    val lastLoggedInUtcTimestamp: Long,
)

data class DeviceDto(
    @BsonId val _id: ObjectId,
    val userId: String,
    val createdUtcTimestamp: Long,
    val lastLoggedInUtcTimestamp: Long,
)

fun Device.toDto(userId: String) = DeviceDto(
    _id = id.toObjectId(),
    userId = userId,
    createdUtcTimestamp = createdUtcTimestamp,
    lastLoggedInUtcTimestamp = lastLoggedInUtcTimestamp
)

fun DeviceDto.toDevice() = Device(
    id = _id.toString(),
    createdUtcTimestamp = createdUtcTimestamp,
    lastLoggedInUtcTimestamp = lastLoggedInUtcTimestamp
)