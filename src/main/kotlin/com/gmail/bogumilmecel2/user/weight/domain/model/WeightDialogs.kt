package com.gmail.bogumilmecel2.user.weight.domain.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WeightDialogs(
    val accepted: Boolean?,
    val lastTimeAsked: String,
    val askedCount: Int
)

data class WeightDialogsDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val userId: String,
    val accepted: Boolean?,
    val lastTimeAsked: String,
    val askedCount: Int
)

fun WeightDialogs.toDto(userId: String) = WeightDialogsDto(
    userId = userId,
    accepted = accepted,
    lastTimeAsked = lastTimeAsked,
    askedCount = askedCount
)

fun WeightDialogsDto.toWeightDialogs() = WeightDialogs(
    accepted = accepted,
    lastTimeAsked = lastTimeAsked,
    askedCount = askedCount
)