package com.gmail.bogumilmecel2.common.util

import kotlinx.datetime.*
import org.bson.conversions.Bson
import org.litote.kmongo.and
import org.litote.kmongo.gte
import org.litote.kmongo.lt
import kotlin.reflect.KProperty1

object CustomDateUtils {

    fun getUtcDate() = getUtcDateTime().date

    fun getUtcDateTime() = getClockNow().toLocalDateTime(TimeZone.UTC)

    fun getTimeZoneDate(timeZone: TimeZone) = try {
        getClockNow()
            .toLocalDateTime(timeZone)
            .date
    } catch (e: Exception) {
        null
    }

    fun LocalDateTime?.getOrMinDateTime() = this ?: LocalDateTime.parse("2023-01-01T00:00:00.000")

    private fun getClockNow() = Clock.System.now()

    fun dateTimeInDay(
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
        kProperty1: KProperty1<*, LocalDate?>,
        date: LocalDate
    ): Bson {
        val nextDayDate = date.plus(DatePeriod(days = 1))

        return and(
            kProperty1 gte LocalDate.parse("${date}T00:00:00.000"),
            kProperty1 lt LocalDate.parse("${nextDayDate}T00:00:00.000")
        )
    }

    fun LocalDate.minusDays(days: Int) = this.minus(DatePeriod(days = days))
}