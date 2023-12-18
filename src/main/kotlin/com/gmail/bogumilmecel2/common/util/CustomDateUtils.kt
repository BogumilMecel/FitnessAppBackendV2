package com.gmail.bogumilmecel2.common.util

import kotlinx.datetime.*
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.gte
import org.litote.kmongo.lt
import kotlin.math.absoluteValue
import kotlin.reflect.KProperty1

object CustomDateUtils {

    fun getCurrentUtcTimestamp() =
        getClockNow()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(UtcOffset.ZERO)
            .toEpochMilliseconds()

    fun getCurrentUtcLocalDate() = getClockNow()
        .toLocalDateTime(timeZone = TimeZone.UTC)
        .date

    fun getCurrentTimeZoneLocalDate(timeZone: TimeZone) = try {
        getClockNow()
            .toLocalDateTime(timeZone)
            .date
    } catch (e: Exception) {
        getCurrentUtcLocalDate()
    }

    fun getDaysFromNow(from: LocalDate) = getCurrentUtcLocalDate().daysUntil(from).absoluteValue

    fun String.isValidDate(): Boolean = this.matches(regex = ("""^\d{4}-\d{2}-\d{2}$""").toRegex())

    private fun getClockNow() = Clock.System.now()

    fun dateInDay(
        kProperty1: KProperty1<*, LocalDateTime?>,
        date: LocalDate
    ): Bson {
        val nextDayDate = date.plus(DatePeriod(days = 1))

        return and(
            kProperty1 gte LocalDateTime.parse("${date}T00:00:00.000"),
            kProperty1 lt LocalDateTime.parse("${nextDayDate}T00:00:00.000")
        )
    }

    fun dateInDay(
        kProperty1: KProperty1<*, LocalDateTime?>,
        date: String
    ) = dateInDay(kProperty1, LocalDate.parse(date))
}