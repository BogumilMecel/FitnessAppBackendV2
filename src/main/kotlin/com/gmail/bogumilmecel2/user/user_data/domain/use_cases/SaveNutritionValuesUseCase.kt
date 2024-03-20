package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class SaveNutritionValuesUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(nutritionValues: NutritionValues, userId: String): Resource<Boolean>{
        return if (nutritionValues.calories < 0) {
            Resource.Error()
        } else if (nutritionValues.carbohydrates < 0) {
            Resource.Error()
        } else if (nutritionValues.protein < 0) {
            Resource.Error()
        } else if (nutritionValues.fat < 0) {
            Resource.Error()
        } else {
            userRepository.saveUserNutritionValues(nutritionValues, userId)
        }
    }
}