package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.assertIs

open class BaseTest {
    val timeZone = TimeZone.UTC

    fun mockDateTime(dateTime: LocalDateTime) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getUtcDateTime() } returns dateTime
    }

    fun mockDate(date: LocalDate) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getUtcDate() } returns date
    }

    fun mockTimeZoneDate(date: LocalDate) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getTimeZoneDate(timeZone = timeZone) } returns date
    }

    fun <T> Resource<T>.assertIsError() {
        assertIs<Resource.Error<T>>(this)
    }

    fun <T> Resource<T>.assertIsSuccess() {
        assertIs<Resource.Success<T>>(this)
    }
}