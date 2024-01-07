package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.domain.constants.Constants.Diary.MAXIMUM_MODIFY_DATE
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.minusDays
import kotlinx.datetime.LocalDate

class IsDateInValidRangeUseCaseUseCase {
    operator fun invoke(
        date: LocalDate,
        range: Int = MAXIMUM_MODIFY_DATE
    ): Boolean {
        return date > CustomDateUtils.getUtcDate().minusDays(days = range)
    }
}