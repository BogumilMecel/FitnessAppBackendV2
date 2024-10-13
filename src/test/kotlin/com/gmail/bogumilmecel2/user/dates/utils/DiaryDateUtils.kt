package com.gmail.bogumilmecel2.user.dates.utils

import kotlinx.datetime.LocalDate

fun createAvailableDiaryDates(daysCount: Int) = (1..daysCount).map { dayOfMonth ->
    LocalDate(year = 2024, monthNumber = 1, dayOfMonth = dayOfMonth)
}