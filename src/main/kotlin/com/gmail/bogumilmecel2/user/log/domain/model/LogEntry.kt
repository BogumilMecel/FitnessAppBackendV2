package com.gmail.bogumilmecel2.user.log.domain.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class LogEntry(
    val date: String = ""
)

data class LogEntryDto(
    @BsonId val _id: ObjectId,
    val userId: String,
    val date: String
)

fun LogEntryDto.toLogEntry() = LogEntry(
    date = date
)

fun LogEntry.toDto(userId: String) = LogEntryDto(
    _id = ObjectId(),
    userId = userId,
    date = date
)
