package com.gmail.bogumilmecel2.authentication.domain.model.user

import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class User(
    @SerialName("id")
    val id: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues? = null,

    @SerialName("user_information")
    val userInformation: UserInformation? = null,

    @SerialName("log_streak")
    val logStreak: Int? = null,

    @SerialName("latest_weight_entry")
    val latestWeightEntry: WeightEntry? = null,

    @SerialName("weight_progress")
    val weightProgress: Double? = null,

    @SerialName("ask_for_weight_daily")
    val askForWeightDaily: Boolean? = null,
)

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

fun UserDto.toUser(): User = User(
    id = _id.toString(),
    email = email,
    username = username,
    nutritionValues = nutritionValues,
    userInformation = userInformation,
    logStreak = logStreak,
    askForWeightDaily = askForWeightDaily,
    weightProgress = weightProgress
)