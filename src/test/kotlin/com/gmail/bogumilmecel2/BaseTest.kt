package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.common.domain.model.exceptions.BaseException
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertIs
import kotlin.test.assertTrue

open class BaseTest {
    val timeZone = TimeZone.UTC

    @BeforeEach
    fun setUpMockk() {
        MockKAnnotations.init()
    }

    @AfterEach
    fun closeMockk() {
        unmockkAll()
    }

    fun mockDateTime(dateTime: LocalDateTime) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getUtcDateTime() } returns dateTime
    }

    fun mockDate(date: LocalDate) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getUtcDate() } returns date
    }

    fun mockTimeZoneDate(date: LocalDate?) {
        mockkObject(CustomDateUtils)
        every { CustomDateUtils.getTimeZoneDate(timeZone = timeZone) } returns date
    }

    fun <T> Resource<T>.assertIsError(exception: BaseException? = null) {
        assertIs<Resource.Error<T>>(this)
        exception?.let {
            assertTrue(message = "Checking for $exception") { exception::class.isInstance(this.exception) }
        }
    }

    fun <T> Resource<T>.assertIsSuccess() {
        assertIs<Resource.Success<T>>(this)
    }
}