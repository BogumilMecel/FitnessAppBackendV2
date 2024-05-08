package com.gmail.bogumilmecel2.authentication.domain.model.user

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class User(
    @SerialName("id")
    val id: String,

    @SerialName("email")
    val email: String,

    @SerialName("username")
    val username: String,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues?,

    @SerialName("user_information")
    val userInformation: UserInformation?,

    @SerialName("log_streak")
    val logStreak: Int,

    @SerialName("latest_weight_entry")
    val latestWeightEntry: WeightEntry?,

    @SerialName("weight_progress")
    val weightProgress: Double?,

    @SerialName("ask_for_weight_daily")
    val askForWeightDaily: Boolean?,
)

@Suppress("PropertyName")
data class UserDto(
    val _id: ObjectId,
    val email: String,
    val username: String,
    val password: String,
    val salt: String,
    val logStreak: Int = 1,
    val nutritionValues: NutritionValues? = null,
    val userInformation: UserInformation? = null,
    val askForWeightDaily: Boolean? = null,
    val weightProgress: Double? = null,
)

fun UserDto.toUser(
    latestWeightEntry: WeightEntry?,
    logStreak: Int? = null
): User = User(
    id = _id.toString(),
    email = email,
    username = username,
    nutritionValues = nutritionValues,
    userInformation = userInformation,
    logStreak = logStreak ?: this.logStreak,
    askForWeightDaily = askForWeightDaily,
    weightProgress = weightProgress,
    latestWeightEntry = latestWeightEntry
)