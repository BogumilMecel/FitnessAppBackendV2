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
import kotlinx.datetime.LocalDate
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class UserRepositoryImp(
    private val userCol: CoroutineCollection<UserDto>,
    private val weightCol: CoroutineCollection<WeightEntryDto>,
    private val logEntryCol: CoroutineCollection<LogEntryDto>,
    private val weightDialogsQuestionCol: CoroutineCollection<WeightDialogsQuestion>
) : UserRepository, BaseRepository() {

    override suspend fun saveUserInformation(userInformation: UserInformation, userId: String): Resource<Boolean> {
        return handleRequest {
            userCol.updateOneById(
                userId.toObjectId(),
                setValue(User::userInformation, userInformation)
            ).wasAcknowledged()
        }
    }

    override suspend fun saveUserNutritionValues(
        nutritionValues: NutritionValues,
        userId: String
    ): Resource<Boolean> {
        return handleRequest {
            userCol.updateOneById(
                userId.toObjectId(),
                setValue(User::nutritionValues, nutritionValues)
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
            userCol.updateOneById(
                userId.toObjectId(),
                setValue(UserDto::logStreak, streak)
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun getLogEntries(
        limit: Int,
        userId: String
    ): Resource<List<LogEntry>> {
        return handleRequest {
            logEntryCol
                .find(LogEntryDto::userId eq userId)
                .limit(limit)
                .sort(descending(LogEntryDto::date))
                .toList()
                .map {
                    it.toLogEntry()
                }
        }
    }

    override suspend fun getUserByEmail(email: String): Resource<UserDto?> {
        return handleRequest {
            userCol.findOne(User::email eq email)
        }
    }

    override suspend fun registerNewUser(user: UserDto): Resource<Boolean> {
        return handleRequest {
            userCol.insertOne(user).wasAcknowledged()
        }
    }

    override suspend fun addWeightEntry(weightEntry: WeightEntryDto): Resource<Unit> {
        return handleRequest {
            weightCol.insertOne(weightEntry).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun checkIfUsernameExists(username: String): Resource<Boolean> {
        return handleRequest {
            userCol.find(UserDto::username eq username).toList().isEmpty()
        }
    }

    override suspend fun getUser(userId: String): Resource<User?> {
        return handleRequest {
            userCol.findOneById(userId.toObjectId())?.toUser()
        }
    }

    override suspend fun getUsername(userId: String): Resource<String?> {
        return handleRequest {
            getUser(userId = userId).data?.username
        }
    }

    override suspend fun getWeightEntries(userId: String, limit: Int): Resource<List<WeightEntryDto>> {
        return handleRequest {
            weightCol.find(WeightEntryDto::userId eq userId)
                .limit(limit)
                .sort(
                    descending(
                        WeightEntryDto::creationDateTime
                    )
                )
                .toList()
        }
    }

    override suspend fun updateAskForWeightDaily(accepted: Boolean, userId: String): Resource<Unit> {
        return handleRequest {
            userCol
                .updateOneById(
                    id = userId.toObjectId(),
                    update = setValue(UserDto::askForWeightDaily, accepted)
                )
                .wasAcknowledgedOrThrow()
        }
    }

    override suspend fun updateWeightProgress(userId: String, weightProgress: Double): Resource<Unit> {
        return handleRequest {
            userCol.updateOneById(
                id = userId.toObjectId(),
                update = setValue(UserDto::weightProgress, weightProgress)
            )
        }
    }

    override suspend fun insertWeightDialogsQuestion(weightDialogsQuestion: WeightDialogsQuestion): Resource<Unit> {
        return handleRequest {
            weightDialogsQuestionCol.insertOne(weightDialogsQuestion)
        }
    }

    override suspend fun getWeightDialogsQuestions(userId: String): Resource<List<WeightDialogsQuestion>> {
        return handleRequest {
            weightDialogsQuestionCol.find(WeightDialogsQuestion::userId eq userId).toList()
        }
    }

    override suspend fun getLatestWeightEntry(userId: String): Resource<WeightEntryDto?> {
        return handleRequest {
            weightCol
                .find(WeightEntryDto::userId eq userId)
                .descendingSort(WeightEntryDto::creationDateTime)
                .limit(1)
                .first()
        }
    }

    override suspend fun removeWeightEntries(userId: String, date: LocalDate): Resource<Unit> {
        return handleRequest {
            weightCol.deleteMany(and(WeightEntryDto::userId eq userId, WeightEntryDto::date eq date.toString()))
        }
    }
}