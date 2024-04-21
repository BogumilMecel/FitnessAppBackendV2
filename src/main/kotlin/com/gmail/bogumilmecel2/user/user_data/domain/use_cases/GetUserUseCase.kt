package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.authentication.domain.model.user.toUser
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UserNotFoundException
import com.gmail.bogumilmecel2.common.util.CustomDateUtils
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.Device
import com.gmail.bogumilmecel2.user.device.domain.repository.DeviceRepository
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntryAndGetLogStreakUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.toWeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.use_case.GetWeightEntriesUseCase
import kotlinx.datetime.TimeZone
import org.bson.types.ObjectId

class GetUserUseCase(
    private val checkLatestLogEntryAndGetLogStreakUseCase: CheckLatestLogEntryAndGetLogStreakUseCase,
    private val getWeightEntriesUseCase: GetWeightEntriesUseCase,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        timezone: TimeZone,
//        deviceId: String
    ): Resource<User> {
        val user = userRepository.getUser(userId = userId).data ?: return Resource.Error(UserNotFoundException)

//        val deviceResource = deviceRepository.getDevice(userId = userId, deviceId = deviceId)

        val currentDate = CustomDateUtils.getUtcDateTime()

//        if (deviceResource is Resource.Success) {
//            if (deviceResource.data != null) {
//                deviceRepository.updateLastLoggedInDateTime(
//                    deviceId = deviceId,
//                    userId = userId,
//                    date = currentDate
//                )
//            } else {
//                deviceRepository.insertDevice(
//                    userId = userId,
//                    device = Device(
//                        _id = ObjectId(),
//                        deviceId = deviceId,
//                        creationDate = currentDate,
//                        userId = userId,
//                        lastLoggedInDate = currentDate,
//                    )
//                )
//            }
//        }

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
            data = user.toUser().copy(
                logStreak = if (logEntryResource is Resource.Success) logEntryResource.data else 1,
                latestWeightEntry = weightEntriesResource.data?.firstOrNull()?.toWeightEntry(),
            )
        )
    }
}