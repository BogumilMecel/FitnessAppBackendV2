package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.Device
import com.gmail.bogumilmecel2.user.device.domain.repository.DeviceRepository
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntryAndGetLogStreakUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.toWeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.use_case.GetWeightEntriesUseCase
import kotlinx.datetime.TimeZone

class GetUserUseCase(
    private val checkLatestLogEntryAndGetLogStreakUseCase: CheckLatestLogEntryAndGetLogStreakUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val getUserObjectUseCase: GetUserObjectUseCase,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(
        userId: String,
        timezone: TimeZone,
        deviceId: String
    ): Resource<User?> {
        val user = getUserObjectUseCase(userId = userId).data ?: return Resource.Error()

        val device = deviceRepository.getDevice(userId = userId, deviceId = deviceId).data

        val currentDate = CustomDateUtils.getUtcDateTime()

        if (device != null) {
            deviceRepository.updateLastLoggedInDateTime(
                deviceId = deviceId,
                userId = userId,
                date = currentDate
            )
        } else {
            deviceRepository.insertDevice(
                userId = userId,
                device = Device(
                    creationDate = currentDate,
                    lastLoggedDate = currentDate,
                )
            )
        }

        val logEntryResource = checkLatestLogEntryAndGetLogStreakUseCase(
            userId = userId,
            timeZone = timezone,
            userStreak = user.logStreak
        )

        val weightEntriesResource = getWeightEntriesUseCase(
            userId = userId,
            limit = 1
        )

        return Resource.Success(
            data = user.copy(
                logStreak = if (logEntryResource is Resource.Success) logEntryResource.data else 1,
                latestWeightEntry = weightEntriesResource.data?.getOrNull(0)?.toWeightEntry(),
            )
        )
    }
}