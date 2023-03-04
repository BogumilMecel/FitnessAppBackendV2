package com.gmail.bogumilmecel2.user.user_data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class IntroductionRequest(
    val gender: Gender,
    val weight: Double,
    val age: Int,
    val height: Int,
    val activityLevel: ActivityLevel,
    val trainingFrequency: TrainingFrequency,
    val typeOfWork: TypeOfWork,
    val desiredWeight: DesiredWeight
)