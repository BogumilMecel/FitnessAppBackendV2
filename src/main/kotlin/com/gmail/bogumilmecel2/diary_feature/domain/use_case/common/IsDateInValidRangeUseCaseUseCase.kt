package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.domain.constants.Constants.Diary.MAXIMUM_MODIFY_DATE
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.CustomDateUtils.minusDays
import kotlinx.datetime.LocalDate

class IsDateInValidRangeUseCaseUseCase {
    operator fun invoke(date: LocalDate): Boolean {
        return date > CustomDateUtils.getCurrentUtcLocalDate().minusDays(days = MAXIMUM_MODIFY_DATE)
    }
}