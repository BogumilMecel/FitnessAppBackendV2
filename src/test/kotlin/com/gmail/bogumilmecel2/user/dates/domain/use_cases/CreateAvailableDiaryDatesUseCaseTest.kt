package com.gmail.bogumilmecel2.user.dates.domain.use_cases

import com.gmail.bogumilmecel2.BaseTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class CreateAvailableDiaryDatesUseCaseTest: BaseTest() {

    private val createAvailableDiaryDatesUseCase = CreateAvailableDiaryDatesUseCase()

    @ParameterizedTest
    @ValueSource(ints = [2, 3])
    fun `Check if available day are dividable by 2 resource error is returned`(availableDaysCount: Int) {
        mockTimeZoneDate(LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 1))
        callTestedMethod(availableDaysCount = availableDaysCount).run {
            if (availableDaysCount % 2 == 0) {
                assertIsError()
            } else {
                assertIsSuccess()
            }
        }
    }

    @Test
    fun `Check if current day is null resource error is returned`() {
        mockTimeZoneDate(date = null)
        callTestedMethod(availableDaysCount = 3).assertIsError()
    }

    @Test
    fun `Check if correct data is returned`() {
        mockTimeZoneDate(date = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 15))
        callTestedMethod(availableDaysCount = 29).run {
            assertIsSuccess()
            assertEquals(
                expected = (1..29).map { dayOfMonth ->
                    LocalDate(year = 2024, monthNumber = 1, dayOfMonth = dayOfMonth)
                },
                actual = data,
            )
        }
    }

    private fun callTestedMethod(availableDaysCount: Int) = createAvailableDiaryDatesUseCase(
        availableDaysCount = availableDaysCount,
        timeZone = TimeZone.UTC,
    )
}