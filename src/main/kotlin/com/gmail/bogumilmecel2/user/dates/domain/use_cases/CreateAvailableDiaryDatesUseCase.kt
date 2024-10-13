package com.gmail.bogumilmecel2.user.dates.domain.use_cases

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class CreateAvailableDiaryDatesUseCase {
    operator fun invoke(
        availableDaysCount: Int,
        timeZone: TimeZone,
    ): Resource<List<LocalDate>> {
        val currentDay = CustomDateUtils.getTimeZoneDate(timeZone)

        if (availableDaysCount % 2 == 0) return Resource.Error()

        if (currentDay == null) return Resource.Error()

        return Resource.Success(
            data = buildList {
                currentDay.minus(DatePeriod(days = availableDaysCount / 2)).let { startingDate ->
                    repeat(availableDaysCount) { dateIndex ->
                        add(startingDate.plus(value = dateIndex, unit = DateTimeUnit.DAY))
                    }
                }
            }
        )
    }
}