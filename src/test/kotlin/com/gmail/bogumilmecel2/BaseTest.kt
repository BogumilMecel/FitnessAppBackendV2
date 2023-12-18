package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.assertIs

open class BaseTest {
    val timeZone = TimeZone.UTC
    fun mockLocalDate(
        value: String = MockConstants.MOCK_DATE_2021,
        utcTimestamp: Long = 0,
    ) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString() } returns value
        every { CustomDateUtils.getCurrentUtcLocalDate() } returns LocalDate.parse(value)
        every { CustomDateUtils.getCurrentUtcTimestamp() } returns utcTimestamp
    }

    fun <T> Resource<T>.assertIsError() {
        assertIs<Resource.Error<T>>(this)
    }

    fun <T> Resource<T>.assertIsSuccess() {
        assertIs<Resource.Success<T>>(this)
    }
}