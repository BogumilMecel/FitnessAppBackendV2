package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.constants.ValidationConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLogEntriesUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogs
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Test
import kotlin.test.assertIs

class CheckIfShouldAskForWeightDialogsUseCaseTest: BaseTest() {

    private val userRepository = mockk<UserRepository>()
    private val getLogEntriesUseCase = mockkClass(GetLogEntriesUseCase::class)
    private val getWeightEntriesUseCase = mockkClass(GetWeightEntriesUseCase::class)

    private val checkIfShouldAskForWeightDialogsUseCase = CheckIfShouldAskForWeightDialogsUseCase(
        getLogEntriesUseCase = getLogEntriesUseCase,
        getWeightEntriesUseCase = getWeightEntriesUseCase,
        userRepository = userRepository
    )

    @Test
    fun `check if get user returns resource error, resource error is returned`() = runTest {
        mockClasses(userWeightDialogsResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestesUseCase())
    }

    @Test
    fun `check if get data returns resource success with already accepted weight dialogs, resource error is returned`() =
        runTest {
            mockClasses(userWeightDialogsResource = Resource.Success(data = mockWeightDialogs(accepted = true)))
            assertIs<Resource.Error<Unit>>(callTestesUseCase())
        }

    @Test
    fun `check if get data returns resource success with already asked for weight dialogs today, resource error is returned`() =
        runTest {
            mockLocalDate(value = MockConstants.MOCK_DATE_2021)
            mockClasses(userWeightDialogsResource = Resource.Success(data = mockWeightDialogs(lastTimeAsked = MockConstants.MOCK_DATE_2021)))
            assertIs<Resource.Error<Unit>>(callTestesUseCase())
        }

    @Test
    fun `check if get data returns resource success with already asked for weight dialogs 3 times, resource error is returned`() =
        runTest {
            mockClasses(userWeightDialogsResource = Resource.Success(data = mockWeightDialogs(askedCount = ValidationConstants.Weight.MINIMUM_ENTRIES_COUNT)))
            assertIs<Resource.Error<Unit>>(callTestesUseCase())
        }

    @Test
    fun `check if get log entries return resource error, resource error is returned`() = runTest {
        mockClasses(logEntriesResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestesUseCase())
    }

    @Test
    fun `check if get log entries return resource success with empty list, resource error is returned`() = runTest {
        mockClasses(logEntriesResource = Resource.Success(data = emptyList()))
        assertIs<Resource.Error<Unit>>(callTestesUseCase())
    }

    @Test
    fun `check if get weight entries return resource error, resource error is returned`() = runTest {
        mockClasses(weightEntriesResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestesUseCase())
    }

    @Test
    fun `check if get weight entries return resource success with empty list, resource error is returned`() = runTest {
        mockClasses(weightEntriesResource = Resource.Success(data = emptyList()))
        assertIs<Resource.Error<Unit>>(callTestesUseCase())
    }

    @Test
    fun `check if other use cases return correct data, resource success is returned`() = runTest {
        mockLocalDate(value = MockConstants.MOCK_DATE_2022)
        mockClasses()
        assertIs<Resource.Success<Unit>>(callTestesUseCase())
    }

    private suspend fun callTestesUseCase() = checkIfShouldAskForWeightDialogsUseCase(
        userId = MockConstants.USER_ID,
        timeZone = TimeZone.UTC
    )

    private fun mockClasses(
        userWeightDialogsResource: Resource<WeightDialogs?> = Resource.Success(
            data = WeightDialogs(
                accepted = false,
                askedCount = 1,
                lastTimeAsked = MockConstants.MOCK_DATE_2021
            )
        ),
        logEntriesResource: Resource<List<LogEntry>> = Resource.Success(data = getSampleLogEntries()),
        weightEntriesResource: Resource<List<WeightEntry>> = Resource.Success(data = getSampleWeightEntries())
    ) {
        coEvery { userRepository.getWeightDialogsData(MockConstants.USER_ID) } returns userWeightDialogsResource
        coEvery {
            getLogEntriesUseCase(
                userId = MockConstants.USER_ID,
                limit = ValidationConstants.Weight.MINIMUM_ENTRIES_COUNT
            )
        } returns logEntriesResource
        coEvery {
            getWeightEntriesUseCase(
                userId = MockConstants.USER_ID,
                limit = ValidationConstants.Weight.MINIMUM_ENTRIES_COUNT
            )
        } returns weightEntriesResource
    }

    private fun mockWeightDialogs(
        accepted: Boolean = false,
        askedCount: Int = 1,
        lastTimeAsked: String = MockConstants.MOCK_DATE_2021
    ) = WeightDialogs(
        accepted = accepted,
        askedCount = askedCount,
        lastTimeAsked = lastTimeAsked
    )

    private fun getSampleLogEntries() = listOf(
        LogEntry(),
        LogEntry()
    )

    private fun getSampleWeightEntries() = listOf(
        WeightEntry(),
        WeightEntry()
    )
}