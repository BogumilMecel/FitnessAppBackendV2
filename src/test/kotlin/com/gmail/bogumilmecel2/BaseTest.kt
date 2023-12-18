package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.TimeZone

open class BaseTest {
    val timeZone = TimeZone.UTC
    fun mockLocalDate(value: String) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getCurrentTimeZoneLocalDate(timeZone).toString() } returns value
    }
}