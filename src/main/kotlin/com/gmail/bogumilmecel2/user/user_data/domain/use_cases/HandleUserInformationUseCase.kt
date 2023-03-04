package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.model.IntroductionRequest
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class HandleUserInformationUseCase(
    private val userRepository: UserRepository,
    private val calculateNutritionValuesUseCase: CalculateNutritionValuesUseCase,
    private val saveNutritionValuesUseCase: SaveNutritionValuesUseCase
) {

    suspend operator fun invoke(
        introductionRequest: IntroductionRequest,
        userId: String
    ): Resource<User> = with(introductionRequest){
        return@with if (age < 0 || age > 100) {
            Resource.Error()
        } else if (height < 0 || height > 250) {
            Resource.Error()
        } else if (weight < 0 || weight > 500) {
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
            userRepository.saveUserInformation(
                userInformation = UserInformation(
                    activityLevel = activityLevel,
                    typeOfWork = typeOfWork,
                    trainingFrequency = trainingFrequency,
                    gender = gender,
                    height = height,
                    currentWeight = weight,
                    desiredWeight = desiredWeight,
                    age = age
                ),
                userId = userId
            )

            if (nutritionValuesResource.data == true) {
                userRepository.getUser(userId = userId).data?.let {
                    Resource.Success(it)
                } ?: Resource.Error()
            } else Resource.Error()
        }
    }
}