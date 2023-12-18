package com.gmail.bogumilmecel2.user.log.domain.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class LogEntry(
    val streak: Int = 1,
    val utcTimestamp: Long = 0
)

data class LogEntryDto(
    @BsonId val _id: ObjectId,
    val streak: Int,
    val utcTimestamp: Long,
    val userId: String
)

fun LogEntryDto.toLogEntry() = LogEntry(
    streak = streak,
    utcTimestamp = utcTimestamp
)

fun LogEntry.toDto(userId: String) = LogEntryDto(
    _id = ObjectId(),
    streak = streak,
    utcTimestamp = utcTimestamp,
    userId = userId
)
