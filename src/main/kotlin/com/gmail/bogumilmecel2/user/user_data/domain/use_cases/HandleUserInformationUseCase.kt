package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.model.IntroductionRequest
import com.gmail.bogumilmecel2.user.user_data.domain.model.IntroductionResponse
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.use_case.AddWeightEntryUseCase
import kotlinx.datetime.TimeZone

class HandleUserInformationUseCase(
    private val userRepository: UserRepository,
    private val calculateNutritionValuesUseCase: CalculateNutritionValuesFromIntroductionUseCase,
    private val saveNutritionValuesUseCase: SaveNutritionValuesUseCase,
    private val addWeightEntryUseCase: AddWeightEntryUseCase,
    private val checkIfWeightIsValidUseCase: CheckIfWeightIsValidUseCase
) {

    suspend operator fun invoke(
        introductionRequest: IntroductionRequest,
        userId: String,
        timezone: TimeZone
    ): Resource<IntroductionResponse> = with(introductionRequest) {
        return@with if (age < 0 || age > 100) {
            Resource.Error()
        } else if (height < 0 || height > 250) {
            Resource.Error()
        } else if (checkIfWeightIsValidUseCase(weight)) {
            Resource.Error()
        } else {
            val nutritionValues = calculateNutritionValuesUseCase(
                gender = gender,
                currentWeight = weight,
                height = height,
                typeOfWork = typeOfWork,
                trainingFrequency = trainingFrequency,
                activityLevel = activityLevel,
                desiredWeight = desiredWeight,
                age = age
            )

            val nutritionValuesResource = saveNutritionValuesUseCase(
                nutritionValues = nutritionValues,
                userId = userId
            )

            if (nutritionValuesResource is Resource.Error) return Resource.Error()

            addWeightEntryUseCase(
                value = weight,
                date = CustomDateUtils.getTimeZoneDate(timeZone = timezone) ?: return Resource.Error(),
                userId = userId,
            )

            val userInformation = UserInformation(
                activityLevel = activityLevel,
                typeOfWork = typeOfWork,
                trainingFrequency = trainingFrequency,
                gender = gender,
                height = height,
                currentWeight = weight,
                desiredWeight = desiredWeight,
                age = age
            )

            userRepository.saveUserInformation(
                userInformation = userInformation,
                userId = userId
            )

            return Resource.Success(
                data = IntroductionResponse(
                    nutritionValues = nutritionValues,
                    userInformation = userInformation
                )
            )
        }
    }
}