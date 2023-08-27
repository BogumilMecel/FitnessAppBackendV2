package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.domain.constants.Constants.Diary.MAXIMUM_MODIFY_DATE
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import org.apache.commons.lang3.time.DateUtils

class IsTimestampInTwoWeeksUseCase {
    operator fun invoke(timestamp: Long): Boolean {
        val currentUtcTimestamp = CustomDateUtils.getCurrentUtcTimestamp()
        return timestamp >= currentUtcTimestamp - (DateUtils.MILLIS_PER_DAY * MAXIMUM_MODIFY_DATE)
    }
}