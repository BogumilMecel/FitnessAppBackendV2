package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import org.apache.commons.lang3.time.DateUtils
import java.util.*

class CheckLatestLogEntry(
    private val getLatestLogEntry: GetLatestLogEntry,
    private val insertLogEntry: InsertLogEntry
) {

    suspend operator fun invoke(userId: String, timestamp: Long): Resource<LogEntry> {
        var finalLogEntry = LogEntry(timestamp = timestamp)
        val latestLogEntryResource = getLatestLogEntry(userId)
        if (latestLogEntryResource is Resource.Success) {
            latestLogEntryResource.data?.let { logEntry ->
                if (DateUtils.isSameDay(Date(timestamp), Date(logEntry.timestamp))) {
                    finalLogEntry = logEntry.copy(
                        timestamp = timestamp
                    )
                } else if (DateUtils.isSameDay(Date(timestamp - DateUtils.MILLIS_PER_DAY), Date(logEntry.timestamp))) {
                    finalLogEntry = LogEntry(timestamp = timestamp, streak = logEntry.streak + 1)
                }
            }
        }
        val newLogEntryResource = insertLogEntry(newLogEntry = finalLogEntry, userId = userId)
        return Resource.Success(
            data = newLogEntryResource.data ?: (latestLogEntryResource.data ?: LogEntry(
                timestamp = timestamp
            ))
        )
    }
}