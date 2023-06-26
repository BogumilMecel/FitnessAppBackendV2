package com.gmail.bogumilmecel2.authentication.domain.model.user

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val username:String = "",
    val nutritionValues: NutritionValues? = null,
    val userInformation: UserInformation? = null,
    val favoriteUserRecipesIds: List<String>,
    val latestLogEntry: LogEntry? = null,
    val latestWeightEntry: WeightEntry? = null,
    val weightProgress: String? = null
)

data class UserDto(
    val _id: ObjectId,
    val email: String,
    val username:String,
    val password:String,
    val salt:String,
    val nutritionValues: NutritionValues? = null,
    val userInformation: UserInformation? = null,
    val favoriteUserRecipesIds: List<String> = emptyList(),
)

fun User.toDto(password: String, salt: String): UserDto = UserDto(
    _id = ObjectId(),
    username = username,
    nutritionValues = nutritionValues,
    userInformation = userInformation,
    email = email,
    password = password,
    salt = salt,
    favoriteUserRecipesIds = favoriteUserRecipesIds
)

fun UserDto.toUser():User = User(
    id = _id.toString(),
    email = email,
    username = username,
    nutritionValues = nutritionValues,
    userInformation = userInformation,
    favoriteUserRecipesIds = favoriteUserRecipesIds
)
