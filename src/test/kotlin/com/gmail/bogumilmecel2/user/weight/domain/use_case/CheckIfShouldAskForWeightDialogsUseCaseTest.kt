package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.domain.constants.ValidationConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntry
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLogEntriesUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.GetUserObjectUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class CheckIfShouldAskForWeightDialogsUseCaseTest {

    private val getUserObjectUseCase = mockkClass(GetUserObjectUseCase::class)
    private val getLogEntriesUseCase = mockkClass(GetLogEntriesUseCase::class)
    private val getWeightEntriesUseCase = mockkClass(GetWeightEntriesUseCase::class)

    private val checkIfShouldAskForWeightDialogsUseCase = CheckIfShouldAskForWeightDialogsUseCase(
        getUserObjectUseCase = getUserObjectUseCase,
        getLogEntriesUseCase = getLogEntriesUseCase,
        getWeightEntriesUseCase = getWeightEntriesUseCase
    )

    @Test
    fun `check if get user returns null resource error is returned`() = runTest {
        mockClasses(userResource = Resource.Success(data = null))
        assertIs<Resource.Error<User?>>(callTestesUseCase())
    }

    @Test
    fun `check if get user returns resource error, resource error is returned`() = runTest {
        mockClasses(userResource = Resource.Error())
        assertIs<Resource.Error<User?>>(callTestesUseCase())
    }

    @Test
    fun `check if get user returns resource success with user that has already been asked for weight dialogs, resource error is returned`() =
        runTest {
            mockClasses(userResource = Resource.Success(data = User(askedForWeightDialogs = true)))
            assertIs<Resource.Error<User?>>(callTestesUseCase())
        }

    @Test
    fun `check if get log entries return resource error, resource error is returned`() = runTest {
        mockClasses(logEntriesResource = Resource.Error())
        assertIs<Resource.Error<User?>>(callTestesUseCase())
    }

    @Test
    fun `check if get log entries return resource success with empty list, resource error is returned`() = runTest {
        mockClasses(logEntriesResource = Resource.Success(data = emptyList()))
        assertIs<Resource.Error<User?>>(callTestesUseCase())
    }

    @Test
    fun `check if get weight entries return resource error, resource error is returned`() = runTest {
        mockClasses(weightEntriesResource = Resource.Error())
        assertIs<Resource.Error<User?>>(callTestesUseCase())
    }

    @Test
    fun `check if get weight entries return resource success with empty list, resource error is returned`() = runTest {
        mockClasses(weightEntriesResource = Resource.Success(data = emptyList()))
        assertIs<Resource.Error<User?>>(callTestesUseCase())
    }

    @Test
    fun `check if other use cases return correct data, resource success is returned`() = runTest {
        mockClasses()
        assertIs<Resource.Success<User?>>(callTestesUseCase())
    }

    private suspend fun callTestesUseCase() = checkIfShouldAskForWeightDialogsUseCase(userId = MockConstants.USER_ID)

    private fun mockClasses(
        userResource: Resource<User?> = Resource.Success(data = User()),
        logEntriesResource: Resource<List<LogEntry>> = Resource.Success(data = getSampleLogEntries()),
        weightEntriesResource: Resource<List<WeightEntry>> = Resource.Success(data = getSampleWeightEntries())
    ) {
        coEvery { getUserObjectUseCase(MockConstants.USER_ID) } returns userResource
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

    private fun getSampleLogEntries() = listOf(
        LogEntry(),
        LogEntry()
    )

    private fun getSampleWeightEntries() = listOf(
        WeightEntry(),
        WeightEntry()
    )
}