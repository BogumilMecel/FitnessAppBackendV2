package com.gmail.bogumilmecel2.user.user_data.domain.repository

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsQuestion
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import kotlinx.datetime.LocalDate

interface UserRepository {
    suspend fun saveUserNutritionValues(nutritionValues: NutritionValues, userId: String): Resource<Boolean>
    suspend fun saveUserInformation(userInformation: UserInformation, userId: String): Resource<Boolean>
    suspend fun saveLogEntry(entry: LogEntry, userId: String): Resource<Unit>
    suspend fun updateUserLogStreak(userId: String, streak: Int): Resource<Unit>
    suspend fun getLogEntries(limit: Int, userId: String): Resource<List<LogEntry>>
    suspend fun getUserByEmail(email: String): Resource<UserDto?>
    suspend fun registerNewUser(user: UserDto): Resource<Boolean>
    suspend fun addWeightEntry(weightEntry: WeightEntryDto): Resource<Unit>
    suspend fun checkIfUsernameExists(username: String): Resource<Boolean>
    suspend fun getUser(userId: String): Resource<User?>
    suspend fun getUsername(userId: String): Resource<String?>
    suspend fun getWeightEntries(userId: String, limit: Int): Resource<List<WeightEntryDto>>
    suspend fun removeWeightEntries(userId: String, date: LocalDate): Resource<Unit>
    suspend fun getLatestWeightEntry(userId: String): Resource<WeightEntryDto?>
    suspend fun updateAskForWeightDaily(accepted: Boolean, userId: String): Resource<Unit>
    suspend fun updateWeightProgress(userId: String, weightProgress: Double): Resource<Unit>
    suspend fun insertWeightDialogsQuestion(weightDialogsQuestion: WeightDialogsQuestion): Resource<Unit>
    suspend fun getWeightDialogsQuestions(userId: String): Resource<List<WeightDialogsQuestion>>
}