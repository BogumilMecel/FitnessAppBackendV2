package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.domain.constants.ValidationConstants
import com.gmail.bogumilmecel2.common.util.extensions.isLengthInRange

class IsDiaryNameValidUseCase {

    operator fun invoke(name: String) = name.isLengthInRange(
        minimum = ValidationConstants.Diary.DIARY_NAME_MIN_LENGTH,
        maximum = ValidationConstants.Diary.DIARY_NAME_MAX_LENGTH
    )
}