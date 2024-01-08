package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UserNotFoundException
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.Device
import com.gmail.bogumilmecel2.user.device.domain.repository.DeviceRepository
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntryAndGetLogStreakUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import com.gmail.bogumilmecel2.user.weight.domain.use_case.GetWeightEntriesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Test

class GetUserUseCaseTest : BaseTest() {

    private val checkLatestLogEntryAndGetLogStreakUseCase = mockkClass(CheckLatestLogEntryAndGetLogStreakUseCase::class)
    private val getWeightEntriesUseCase = mockkClass(GetWeightEntriesUseCase::class)
    private val deviceRepository = mockk<DeviceRepository>()
    private val userRepository = mockk<UserRepository>()
    private val getUserUseCase = GetUserUseCase(
        checkLatestLogEntryAndGetLogStreakUseCase = checkLatestLogEntryAndGetLogStreakUseCase,
        deviceRepository = deviceRepository,
        getWeightEntriesUseCase = getWeightEntriesUseCase,
        userRepository = userRepository
    )

    @Test
    fun `check if user repository returns error, resource error is returned`() = runTest {
        mockData(userResource = Resource.Error())
        callTestedMethod().assertIsError(UserNotFoundException)
    }

    @Test
    fun `check if user repository returns null, resource error is returned`() = runTest {
        mockData(userResource = Resource.Success(data = null))
        callTestedMethod().assertIsError(UserNotFoundException)
    }

    @Test
    fun `check if device is inserted if its repository returns success and device is null`() = runTest {
        mockData(deviceResource = Resource.Success(data = null))
        callTestedMethod()
        coVerify(exactly = 1) {
            deviceRepository.insertDevice(
                userId = MockConstants.USER_ID_1,
                device = any()
            )
        }
    }

    @Test
    fun `check if all methods are called and resource success is returned if user is not null`() = runTest {
        mockDateTime(dateTime = MockConstants.DATE_TIME.toLocalDateTime())
        mockData()
        callTestedMethod().assertIsSuccess()
        coVerify(exactly = 1) {
            deviceRepository.updateLastLoggedInDateTime(
                userId = MockConstants.USER_ID_1,
                date = MockConstants.DATE_TIME.toLocalDateTime(),
                deviceId = any()
            )
            checkLatestLogEntryAndGetLogStreakUseCase(userId = MockConstants.USER_ID_1, timeZone = TimeZone.UTC, userStreak = 1)
            getWeightEntriesUseCase(userId = MockConstants.USER_ID_1, limit = 1)
        }
    }

    private fun mockData(
        userResource: Resource<UserDto?> = Resource.Success(MockConstants.getUser()),
        deviceResource: Resource<Device?> = Resource.Success(MockConstants.getDevice()),
        insertDeviceResource: Resource<Unit> = Resource.Success(Unit),
        updateDeviceResource: Resource<Unit> = Resource.Success(Unit),
        logEntryResource: Resource<Int> = Resource.Success(1),
        weightEntriesResource: Resource<List<WeightEntryDto>> = Resource.Success(MockConstants.Weight.getWeightEntries(14)),
    ) {
        coEvery { userRepository.getUser(userId = MockConstants.USER_ID_1) } returns userResource
        coEvery { deviceRepository.getDevice(userId = MockConstants.USER_ID_1, deviceId = MockConstants.DEVICE_ID) } returns deviceResource
        coEvery {
            deviceRepository.updateLastLoggedInDateTime(
                userId = MockConstants.USER_ID_1,
                deviceId = MockConstants.DEVICE_ID,
                date = MockConstants.DATE_TIME.toLocalDateTime()
            )
        } returns updateDeviceResource
        coEvery {
            deviceRepository.insertDevice(
                userId = MockConstants.USER_ID_1,
                device = any()
            )
        } returns insertDeviceResource
        coEvery {
            checkLatestLogEntryAndGetLogStreakUseCase(
                userId = MockConstants.USER_ID_1,
                timeZone = TimeZone.UTC,
                userStreak = any()
            )
        } returns logEntryResource
        coEvery { getWeightEntriesUseCase(userId = MockConstants.USER_ID_1, limit = 1) } returns weightEntriesResource
    }

    private suspend fun callTestedMethod() = getUserUseCase(
        userId = MockConstants.USER_ID_1,
        timezone = TimeZone.UTC,
        deviceId = MockConstants.DEVICE_ID
    )
}