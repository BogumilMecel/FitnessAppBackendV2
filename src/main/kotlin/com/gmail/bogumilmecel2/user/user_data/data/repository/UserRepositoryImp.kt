package com.gmail.bogumilmecel2.user.user_data.data.repository

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.authentication.domain.model.user.toUser
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntryDto
import com.gmail.bogumilmecel2.user.log.domain.model.toDto
import com.gmail.bogumilmecel2.user.log.domain.model.toLogEntry
import com.gmail.bogumilmecel2.user.user_data.domain.model.UserInformation
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class UserRepositoryImp(
    private val userCol: MongoCollection<UserDto>,
    private val weightCol: MongoCollection<WeightEntryDto>,
    private val logEntryCol: MongoCollection<LogEntryDto>,
) : UserRepository, BaseRepository() {

    override suspend fun saveUserInformation(userInformation: UserInformation, userId: String): Resource<Boolean> {
        return handleRequest {
            userCol.updateOne(
                filter = eq(UserDto::_id.name, userId.toObjectId()),
                update = Updates.set(User::userInformation.name, userInformation)
            ).wasAcknowledged()
        }
    }

    override suspend fun saveUserNutritionValues(
        nutritionValues: NutritionValues,
        userId: String
    ): Resource<Boolean> {
        return handleRequest {
            userCol.updateOne(
                filter = eq(UserDto::_id.name, userId.toObjectId()),
                update = Updates.set(User::nutritionValues.name, nutritionValues)
            ).wasAcknowledged()
        }
    }

    override suspend fun saveLogEntry(entry: LogEntry, userId: String): Resource<Unit> {
        return handleRequest {
            logEntryCol.insertOne(entry.toDto(userId = userId)).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun updateUserLogStreak(userId: String, streak: Int): Resource<Unit> {
        return handleRequest {
            userCol.updateOne(
                filter = eq(UserDto::_id.name, userId.toObjectId()),
                update = Updates.set(User::logStreak.name, streak)
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun getLogEntries(
        limit: Int,
        userId: String
    ): Resource<List<LogEntry>> {
        return handleRequest {
            logEntryCol
                .find(eq(LogEntryDto::userId.name, userId))
                .limit(limit)
                .sort(Sorts.descending(LogEntryDto::date.name))
                .toList()
                .map {
                    it.toLogEntry()
                }
        }
    }

    override suspend fun getUserByEmail(email: String): Resource<UserDto?> {
        return handleRequest {
            userCol.find(eq(User::email.name, email)).firstOrNull()
        }
    }

    override suspend fun registerNewUser(user: UserDto): Resource<Boolean> {
        return handleRequest {
            userCol.insertOne(user).wasAcknowledged()
        }
    }

    override suspend fun addWeightEntry(weightEntry: WeightEntry, userId: String): Resource<Boolean> {
        return handleRequest {
            weightCol.insertOne(weightEntry.toDto(userId = userId)).wasAcknowledged()
        }
    }

    override suspend fun checkIfUsernameExists(username: String): Resource<Boolean> {
        return handleRequest {
            userCol.find(eq(UserDto::username.name, username)).toList().isEmpty()
        }
    }

    override suspend fun getUser(userId: String): Resource<User?> {
        return handleRequest {
            userCol.find(eq(UserDto::_id.name, userId.toObjectId())).firstOrNull()?.toUser()
        }
    }

    override suspend fun getUsername(userId: String): Resource<String?> {
        return handleRequest {
            getUser(userId = userId).data?.username
        }
    }

    override suspend fun getWeightEntries(userId: String, limit: Int): Resource<List<WeightEntry>> {
        return handleRequest {
            weightCol.find(eq(WeightEntryDto::userId.name, userId))
                .limit(limit)
                .sort(Sorts.descending(WeightEntryDto::utcTimestamp.name))
                .toList()
                .map { it.toObject() }
        }
    }

    override suspend fun updateWeightDialogsData(weightDialogs: WeightDialogs, userId: String): Resource<Unit> {
        return handleRequest {
            userCol
                .updateOne(
                    filter = eq(UserDto::_id.name, userId.toObjectId()),
                    update = Updates.set(UserDto::weightDialogs.name, weightDialogs)
                )
                .wasAcknowledgedOrThrow()
        }
    }
}