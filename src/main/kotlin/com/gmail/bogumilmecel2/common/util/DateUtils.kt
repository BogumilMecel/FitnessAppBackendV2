package com.gmail.bogumilmecel2.common.util

import kotlinx.datetime.*

object DateUtils {
    fun getCurrentUtcTimestamp() =
        Clock.System.now().toLocalDateTime(TimeZone.UTC).toInstant(UtcOffset.ZERO).toEpochMilliseconds()

    fun String.isValidDate(): Boolean = this.matches(regex = ("""^\d{4}-\d{2}-\d{2}$""").toRegex())
}