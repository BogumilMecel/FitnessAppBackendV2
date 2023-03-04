package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.user_data.domain.model.*

class CalculateNutritionValuesUseCase {

    operator fun invoke(
        gender: Gender,
        currentWeight: Double,
        height: Int,
        typeOfWork: TypeOfWork,
        trainingFrequency: TrainingFrequency,
        activityLevel: ActivityLevel,
        desiredWeight: DesiredWeight,
        age: Int
    ): NutritionValues {
        val ppm = (10.0 * currentWeight) + (6.25 * height) - (5.0 * age).apply {
            when (gender) {
                Gender.MALE -> this + 5.0
                else -> this - 161.0
            }
        }

        val firstFactor = when (typeOfWork) {
            TypeOfWork.SEDENTARY -> 0.0
            TypeOfWork.MIXED -> 1.0 / 15.0
            TypeOfWork.PHYSICAL -> 2.0 / 15.0
        }

        val secondFactor = when (trainingFrequency) {
            TrainingFrequency.NONE -> 0.0
            TrainingFrequency.LOW -> 1.0 / 30.0
            TrainingFrequency.AVERAGE -> 2.0 / 30.0
            TrainingFrequency.HIGH -> 3.0 / 30.0
            TrainingFrequency.VERY_HIGH -> 4.0 / 30.0
        }

        val thirdFactor = when (activityLevel) {
            ActivityLevel.LOW -> 0.0
            ActivityLevel.MODERATE -> 1.0 / 15.0
            ActivityLevel.HIGH -> 2.0 / 15.0
            ActivityLevel.VERY_HIGH -> 3.0 / 15.0
        }

        val pal = 1.4 + firstFactor + secondFactor + thirdFactor

        val cpm = ppm * pal

        var wantedCalories = when (desiredWeight) {
            DesiredWeight.GAIN -> cpm + 400.0
            DesiredWeight.LOOSE -> cpm - 400.0
            DesiredWeight.KEEP -> cpm + 0.0
        }

        val wantedProtein = (wantedCalories * 30.0 / 100.0 / 4.0).round(2)
        val wantedFat = (wantedCalories * 25.0 / 100.0 / 9.0).round(2)
        val wantedCarbs = ((wantedCalories - ((9.0 * wantedFat) + (4.0 * wantedProtein))) / 4.0).round(2)
        wantedCalories = (wantedCarbs * 4) + (wantedProtein * 4) + (wantedFat * 9)

        return NutritionValues(
            calories = wantedCalories.toInt(),
            carbohydrates = wantedCarbs,
            protein = wantedProtein,
            fat = wantedFat
        )
    }
}