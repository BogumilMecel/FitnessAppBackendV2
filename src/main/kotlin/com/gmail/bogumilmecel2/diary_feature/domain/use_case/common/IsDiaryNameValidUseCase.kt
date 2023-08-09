package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.extensions.isLengthInRange

class IsDiaryNameValidUseCase {

    operator fun invoke(name: String) = name.isLengthInRange(
        minimum = Constants.Diary.DIARY_NAME_MIN_LENGTH,
        maximum = Constants.Diary.DIARY_NAME_MAX_LENGTH
    )
}