package com.gmail.bogumilmecel2.user.user_data.domain.model

import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import kotlinx.serialization.Serializable

@Serializable
data class IntroductionResponse(
    val nutritionValues: NutritionValues,
    val userInformation: UserInformation
)
