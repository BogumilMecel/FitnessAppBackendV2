package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLogEntriesUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsQuestion
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Test
import kotlin.test.assertIs

class CheckIfShouldAskForWeightDialogsUseCaseTestQuestion: BaseTest() {

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
        mockClasses(userResource = Resource.Error())
        callTestesUseCase().assertIsError()
    }

    @Test
    fun `check if get user returns user with accepted daily weight dialogs, resource error is returned`() =
        runTest {
            mockClasses(userResource = Resource.Success(data = User(askForWeightDaily = true)))
            callTestesUseCase().assertIsError()
        }

    @Test
    fun `check if get user returns user with declined daily weight dialogs, resource error is returned`() =
        runTest {
            mockClasses(userResource = Resource.Success(data = User(askForWeightDaily = false)))
            callTestesUseCase().assertIsError()
        }

    @Test
    fun `check if getWeightDialogsQuestions returns resource error, resource error is returned`() =
        runTest {
            mockClasses(getWeightDialogsQuestionsResource = Resource.Error())
            callTestesUseCase().assertIsError()
        }

    @Test
    fun `check if getWeightDialogsQuestions returns list larger than 3, resource error is returned`() =
        runTest {
            mockClasses(getWeightDialogsQuestionsResource = Resource.Success(data = MockConstants.Weight.getWeightDialogsQuestions(count = 4)))
            callTestesUseCase().assertIsError()
        }

    @Test
    fun `check if user has already been asked today, resource error is returned`() =
        runTest {
            mockClasses()
            mockTimeZoneDate(date = MockConstants.getFormattedDate(3))
            callTestesUseCase().assertIsError()
        }

    @Test
    fun `check if get log entries return resource error, resource error is returned`() = runTest {
        mockClasses(logEntriesResource = Resource.Error())
        callTestesUseCase().assertIsError()
    }

    @Test
    fun `check if get log entries return resource success with empty list, resource error is returned`() = runTest {
        mockClasses(logEntriesResource = Resource.Success(data = emptyList()))
        callTestesUseCase().assertIsError()
    }

    @Test
    fun `check if get weight entries return resource error, resource error is returned`() = runTest {
        mockClasses(weightEntriesResource = Resource.Error())
        callTestesUseCase().assertIsError()
    }

    @Test
    fun `check if get weight entries return resource success with empty list, resource error is returned`() = runTest {
        mockClasses(weightEntriesResource = Resource.Success(data = emptyList()))
        callTestesUseCase().assertIsError()
    }

    @Test
    fun `check if other use cases return correct data, resource success is returned`() = runTest {
        mockDate(date = MockConstants.getFormattedDate(value = 1))
        mockClasses()
        assertIs<Resource.Success<Unit>>(callTestesUseCase())
    }

    private suspend fun callTestesUseCase() = checkIfShouldAskForWeightDialogsUseCase(
        userId = MockConstants.USER_ID_1,
        timeZone = TimeZone.UTC
    )

    private fun mockClasses(
        userResource: Resource<User?> = Resource.Success(data = User(askForWeightDaily = null)),
        logEntriesResource: Resource<List<LogEntry>> = Resource.Success(data = getSampleLogEntries()),
        weightEntriesResource: Resource<List<WeightEntry>> = Resource.Success(data = getSampleWeightEntries()),
        getWeightDialogsQuestionsResource: Resource<List<WeightDialogsQuestion>> = Resource.Success(data = MockConstants.Weight.getWeightDialogsQuestions())
    ) {
        coEvery { userRepository.getUser(MockConstants.USER_ID_1) } returns userResource
        coEvery {
            getLogEntriesUseCase(
                userId = MockConstants.USER_ID_1,
                limit = Constants.Weight.MINIMUM_ENTRIES_COUNT
            )
        } returns logEntriesResource
        coEvery {
            getWeightEntriesUseCase(
                userId = MockConstants.USER_ID_1,
                limit = Constants.Weight.MINIMUM_ENTRIES_COUNT
            )
        } returns weightEntriesResource
        coEvery { userRepository.getWeightDialogsQuestions(userId = MockConstants.USER_ID_1) } returns getWeightDialogsQuestionsResource
    }

    private fun getSampleLogEntries() = listOf(
        LogEntry(),
        LogEntry()
    )

    private fun getSampleWeightEntries() = listOf(
        WeightEntry(),
        WeightEntry()
    )
}