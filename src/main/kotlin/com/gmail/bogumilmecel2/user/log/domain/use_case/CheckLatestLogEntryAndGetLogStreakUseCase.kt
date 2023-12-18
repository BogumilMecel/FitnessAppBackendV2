package com.gmail.bogumilmecel2.user.log.domain.use_case

import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

class CheckLatestLogEntryAndGetLogStreakUseCase(
    private val getLogEntriesUseCase: GetLogEntriesUseCase,
    private val insertLogEntryUseCase: InsertLogEntryUseCase,
    private val updateUserLogStreakUseCase: UpdateUserLogStreakUseCase
) {

    suspend operator fun invoke(
        userId: String,
        timeZone: TimeZone,
        userStreak: Int
    ): Resource<Int> {
        val latestLogEntryResource = getLogEntriesUseCase(
            userId = userId,
            limit = 1
        )

        when (latestLogEntryResource) {
            is Resource.Success -> {
                val currentDate = CustomDateUtils.getTimeZoneDate(timeZone = timeZone) ?: return Resource.Error()
                val streak = latestLogEntryResource.data.firstOrNull()?.let { latestLogEntry ->
                    val latestEntryDate = LocalDate.parse(latestLogEntry.date)
                    when {
                        currentDate == latestEntryDate -> {
                            return Resource.Success(userStreak)
                        }

                        currentDate.minus(DatePeriod(days = 1)) == latestEntryDate -> {
                            userStreak + 1
                        }

                        else -> {
                            1
                        }
                    }
                } ?: 1

                val updateStreakResource = updateUserLogStreakUseCase(
                    userId = userId,
                    streak = streak
                )

                return when(updateStreakResource) {
                    is Resource.Success -> {
                        insertLogEntry(
                            logEntry = LogEntry(date = currentDate.toString()),
                            userId = userId,
                            streak = streak
                        )
                    }

                    is Resource.Error -> {
                        Resource.Error()
                    }
                }
            }

            is Resource.Error -> {
                return Resource.Error()
            }
        }
    }

    private suspend fun insertLogEntry(
        logEntry: LogEntry,
        userId: String,
        streak: Int
    ): Resource<Int> {
        val resource = insertLogEntryUseCase(
            newLogEntry = logEntry,
            userId = userId
        )

        return when(resource) {
            is Resource.Success -> {
                Resource.Success(streak)
            }

            is Resource.Error -> {
                Resource.Error()
            }
        }
    }
}