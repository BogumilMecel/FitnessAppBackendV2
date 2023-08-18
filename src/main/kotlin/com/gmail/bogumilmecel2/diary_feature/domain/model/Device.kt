package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Device(
    val id: String,
    val createdUtcTimestamp: Long,
    val lastLoggedInUtcTimestamp: Long,
    val latestDiaryEntryUtcTimestamp: Long
)

data class DeviceDto(
    @BsonId val _id: String,
    val userId: String,
    val createdUtcTimestamp: Long,
    val lastLoggedInUtcTimestamp: Long,
    val latestDiaryEntryUtcTimestamp: Long
)