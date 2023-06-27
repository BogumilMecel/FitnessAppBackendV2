package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

class CheckIfWeightIsValid {
    operator fun invoke(value: Double) = value > 0 && value < 400
}