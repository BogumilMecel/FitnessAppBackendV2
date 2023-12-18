package com.gmail.bogumilmecel2.common.util

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.apache.commons.lang3.time.DateUtils
import java.util.*

object CustomDateUtils {

    fun getCurrentUtcTimestamp() =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(UtcOffset.ZERO)
            .toEpochMilliseconds()

    fun getCurrentTimezoneTimestamp(timezone: TimeZone) = try {
        Clock.System
            .now()
            .toLocalDateTime(timeZone = timezone)
            .toInstant(UtcOffset.ZERO)
            .toEpochMilliseconds()
    } catch (e: Exception) {
        null
    }

    fun isSameDate(first: Long, second: Long) = DateUtils.isSameDay(Date(first), Date(second))

    fun String.isValidDate(): Boolean = this.matches(regex = ("""^\d{4}-\d{2}-\d{2}$""").toRegex())

    fun Long.toTimezone(timezone: TimeZone) = Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(timezone)
        .toInstant(UtcOffset.ZERO)
        .toEpochMilliseconds()
}