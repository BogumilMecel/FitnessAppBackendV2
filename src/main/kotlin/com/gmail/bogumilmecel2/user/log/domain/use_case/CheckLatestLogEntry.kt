package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.toTimezone
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import kotlinx.datetime.TimeZone
import org.apache.commons.lang3.time.DateUtils

class CheckLatestLogEntry(
    private val getLatestLogEntry: GetLatestLogEntry,
    private val insertLogEntry: InsertLogEntry
) {

    suspend operator fun invoke(userId: String, timezone: TimeZone): Resource<LogEntry> {
        when (val latestLogEntryResource = getLatestLogEntry(userId = userId)) {
            is Resource.Success -> {
                val timestamp =
                    CustomDateUtils.getCurrentTimezoneTimestamp(timeZone = timezone) ?: return Resource.Error()
                latestLogEntryResource.data?.let { latestLogEntry ->
                    val latestTimestamp = latestLogEntry.utcTimestamp.toTimezone(timezone = timezone)
                    return when {
                        CustomDateUtils.isSameDate(
                            first = timestamp,
                            second = latestTimestamp
                        ) -> Resource.Success(latestLogEntry)

                        CustomDateUtils.isSameDate(
                            first = timestamp,
                            second = (latestTimestamp - DateUtils.MILLIS_PER_DAY)
                        ) -> insertLogEntry(
                            userId = userId,
                            logEntry = createLogEntryWithCurrentTimestamp(streak = latestLogEntry.streak + 1)
                        )

                        else -> insertLogEntry(
                            userId = userId,
                            logEntry = createLogEntryWithCurrentTimestamp()
                        )
                    }
                } ?: kotlin.run {
                    return insertLogEntry(
                        userId = userId,
                        logEntry = createLogEntryWithCurrentTimestamp()
                    )
                }
            }

            is Resource.Error -> {
                return Resource.Error()
            }
        }
    }

    private fun createLogEntryWithCurrentTimestamp(streak: Int = 1) = LogEntry(
        streak = streak,
        utcTimestamp = CustomDateUtils.getCurrentUtcTimestamp()
    )

    private suspend fun insertLogEntry(
        logEntry: LogEntry,
        userId: String
    ) = insertLogEntry(
        newLogEntry = logEntry,
        userId = userId
    )
}