package com.gmail.bogumilmecel2.user.user_data.domain.repository

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry

interface UserRepository {
    suspend fun saveUserNutritionValues(nutritionValues: NutritionValues, userId: String): Resource<Boolean>
    suspend fun saveUserInformation(userInformation: UserInformation, userId: String): Resource<Boolean>
    suspend fun saveLogEntry(entry: LogEntry, userId: String): Resource<LogEntry>
    suspend fun getLatestLogEntry(userId: String): Resource<LogEntry?>
    suspend fun getUserByEmail(email: String): Resource<UserDto?>
    suspend fun registerNewUser(user: UserDto): Resource<Boolean>
    suspend fun addWeightEntry(weightEntry: WeightEntry, userId: String): Resource<Boolean>
    suspend fun checkIfUsernameExists(username: String): Resource<Boolean>
    suspend fun getUser(userId: String): Resource<User?>
    suspend fun getUsername(userId: String): Resource<String?>
    suspend fun getWeightEntries(userId: String, limit: Int): Resource<List<WeightEntry>>
}