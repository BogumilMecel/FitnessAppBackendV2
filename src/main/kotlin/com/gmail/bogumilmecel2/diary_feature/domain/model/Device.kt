package com.gmail.bogumilmecel2.diary_feature.domain.model

import kotlinx.datetime.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Device(
    @BsonId val _id: ObjectId,
    val deviceId: String,
    val userId: String,
    val creationDate: LocalDateTime,
    val lastLoggedInDate: LocalDateTime,
)