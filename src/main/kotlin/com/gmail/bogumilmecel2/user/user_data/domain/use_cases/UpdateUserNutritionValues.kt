package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository

class UpdateUserNutritionValues(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(nutritionValues: NutritionValues, userId: String):Resource<Boolean>{
        return userRepository.saveUserNutritionValues(nutritionValues = nutritionValues, userId = userId)
    }
}