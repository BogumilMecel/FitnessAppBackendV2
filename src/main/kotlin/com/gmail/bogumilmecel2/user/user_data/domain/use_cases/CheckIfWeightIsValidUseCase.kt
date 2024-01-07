package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.common.domain.constants.Constants.Weight.MAX_WEIGHT_VALUE
import com.gmail.bogumilmecel2.common.domain.constants.Constants.Weight.MIN_WEIGHT_VALUE

class CheckIfWeightIsValidUseCase {
    operator fun invoke(value: Double) = value > MIN_WEIGHT_VALUE && value < MAX_WEIGHT_VALUE
}