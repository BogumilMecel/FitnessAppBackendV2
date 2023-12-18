package com.gmail.bogumilmecel2.common.util

import kotlinx.datetime.*

object CustomDateUtils {

    fun getCurrentUtcTimestamp() =
        getClockNow()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(UtcOffset.ZERO)
            .toEpochMilliseconds()

    private fun getCurrentUtcLocalDateString() = getClockNow()
        .toLocalDateTime(timeZone = TimeZone.UTC)
        .date.toString()

    private fun getCurrentUtcLocalDate() = getClockNow()
        .toLocalDateTime(timeZone = TimeZone.UTC)
        .date

    fun getCurrentTimeZoneLocalDate(timeZone: TimeZone) = try {
        getClockNow()
            .toLocalDateTime(timeZone)
            .date
    } catch (e: Exception) {
        getCurrentUtcLocalDate()
    }

    fun getCurrentTimeZoneLocalDateString(timeZone: TimeZone) = try {
        getClockNow()
            .toLocalDateTime(timeZone)
            .date
            .toString()
    } catch (e: Exception) {
        getCurrentUtcLocalDateString()
    }

    fun String.isValidDate(): Boolean = this.matches(regex = ("""^\d{4}-\d{2}-\d{2}$""").toRegex())

    private fun getClockNow() = Clock.System.now()
}