package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.TimeZone

open class BaseTest {
    val timeZone = TimeZone.UTC
    fun mockLocalDate(value: String = MockConstants.MOCK_DATE_2021, utcTimestamp: Long = 0) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString() } returns value
        every { CustomDateUtils.getCurrentUtcTimestamp() } returns utcTimestamp
    }
}