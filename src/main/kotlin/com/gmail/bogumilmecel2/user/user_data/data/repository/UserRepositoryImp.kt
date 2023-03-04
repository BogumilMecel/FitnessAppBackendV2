package com.gmail.bogumilmecel2.user.user_data.data.repository

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.authentication.domain.model.user.toUser
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import com.gmail.bogumilmecel2.user.weight.domain.model.toDto
import com.gmail.bogumilmecel2.user.weight.domain.model.toObject
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class UserRepositoryImp(
    private val userCol: CoroutineCollection<UserDto>,
    private val weightCol: CoroutineCollection<WeightEntryDto>
) : UserRepository, BaseRepository() {

    override suspend fun saveUserInformation(userInformation: UserInformation, userId: String): Resource<Boolean> {
        return try {
            Resource.Success(
                userCol.updateOneById(
                    userId.toObjectId(),
                    setValue(User::userInformation, userInformation)
                ).wasAcknowledged()
            )
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun saveUserNutritionValues(
        nutritionValues: NutritionValues,
        userId: String
    ): Resource<Boolean> {
        return try {
            println("$userId $nutritionValues")
            Resource.Success(
                userCol.updateOneById(
                    userId.toObjectId(),
                    setValue(User::nutritionValues, nutritionValues)
                ).wasAcknowledged()
            )
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun saveLogEntry(entry: LogEntry, userId: String): Resource<LogEntry> {
        return try {
            userCol.updateOneById(userId.toObjectId(), setValue(UserDto::latestLogEntry, entry))
            Resource.Success(
                data = LogEntry(
                    streak = entry.streak,
                    timestamp = entry.timestamp
                )
            )
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }

    }

    override suspend fun getLatestLogEntry(userId: String): Resource<LogEntry?> {
        return try {
            Resource.Success(data = userCol.findOneById(userId.toObjectId())?.latestLogEntry)
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Resource<UserDto?> {
        return try {
            Resource.Success(userCol.findOne(User::email eq email))
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun registerNewUser(user: UserDto): Resource<Boolean> {
        return try {
            Resource.Success(userCol.insertOne(user).wasAcknowledged())
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun addWeightEntry(weightEntry: WeightEntry, userId: String): Resource<Boolean> {
        return try {
            Resource.Success(data = weightCol.insertOne(weightEntry.toDto(userId = userId)).wasAcknowledged())
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }

    }

    override suspend fun checkIfUsernameExists(username: String): Resource<Boolean> {
        return try {
            Resource.Success(userCol.find(UserDto::username eq username).toList().isEmpty())
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun getUser(userId: String): Resource<User?> {
        return try {
            Resource.Success(userCol.findOneById(userId.toObjectId())?.toUser())
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun getUsername(userId: String): Resource<String?> {
        return try {
            Resource.Success(data = getUser(userId = userId).data?.username)
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }

    override suspend fun getWeightEntries(userId: String, limit: Int): Resource<List<WeightEntry>> {
        return try {
            Resource.Success(
                data = weightCol.find(WeightEntryDto::userId eq userId)
                    .limit(limit)
                    .sort(
                        descending(
                            WeightEntryDto::timestamp
                        )
                    )
                    .toList()
                    .map { it.toObject() })
        } catch (e: Exception) {
            handleExceptionWithResource(e)
        }
    }
}