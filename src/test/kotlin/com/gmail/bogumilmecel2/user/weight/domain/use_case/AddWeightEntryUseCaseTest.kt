package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.domain.model.exceptions.DateNotInRangeException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.InvalidWeightValueException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UserNotFoundException
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.CheckIfWeightIsValidUseCase
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

class AddWeightEntryUseCaseTest: BaseTest() {

    private val userRepository = mockk<UserRepository>()
    private val calculateWeightProgressUseCase = mockkClass(CalculateWeightProgressUseCase::class)
    private val checkIfWeightIsValidUseCase = mockkClass(CheckIfWeightIsValidUseCase::class)
    private val isDateInvValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val addWeightEntryUseCase = AddWeightEntryUseCase(
        userRepository = userRepository,
        calculateWeightProgressUseCase = calculateWeightProgressUseCase,
        checkIfWeightIsValidUseCase = checkIfWeightIsValidUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInvValidRangeUseCaseUseCase,
    )

    @Test
    fun `Check if user does not exist, resource error is returned`() = runTest {
        mockData(userExists = false)
        callTestedMethod().assertIsError(UserNotFoundException)
    }

    @Test
    fun `Check if value is not valid, resource error is returned`() = runTest {
        mockData(valueValid = false)
        callTestedMethod().assertIsError(InvalidWeightValueException)
    }

    @Test
    fun `Check if date is not in range, resource error is returned`() = runTest {
        mockData(dateInRange = false)
        callTestedMethod().assertIsError(DateNotInRangeException)
    }

    @Test
    fun `Check if removal returns resource error, resource error is returned`() = runTest {
        mockData(removalResource = Resource.Error())
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if repository returns resource error when adding, resource error is returned`() = runTest {
        mockData(weightEntryResource = Resource.Error())
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if repository returns resource success when adding and getting weight entries returns resource error, resource success is returned anyway but weight is not calculated`() = runTest {
        mockData(weightEntriesResource = Resource.Error())
        callTestedMethod().assertIsSuccess()
        verifyAfterSuccess(calculateCount = 0)
    }

    @Test
    fun `Check if repository returns resource success when adding and getting weight entries returns resource success, resource success is returned and weight is calculated`() = runTest {
        mockData()
        callTestedMethod().run {
            assertIsSuccess()
            assertEquals(
                actual = data?.weightProgress,
                expected = MockConstants.Weight.VALUE
            )
        }
        verifyAfterSuccess()
    }

    private fun verifyAfterSuccess(
        getWeightEntriesCount: Int = 1,
        calculateCount: Int = 1
    ) {
        coVerify(exactly = getWeightEntriesCount) { userRepository.getWeightEntries(userId = MockConstants.USER_ID_1, limit = 14) }
        coVerify(exactly = calculateCount) { calculateWeightProgressUseCase(userId = MockConstants.USER_ID_1, weightEntries = any()) }
    }

    private fun mockData(
        progress: Double = MockConstants.Weight.VALUE,
        dateInRange: Boolean = true,
        valueValid: Boolean = true,
        userExists: Boolean = true,
        removalResource: Resource<Unit> = Resource.Success(Unit),
        weightEntryResource: Resource<Unit> = Resource.Success(Unit),
        weightEntriesResource: Resource<List<WeightEntryDto>> = Resource.Success(data = MockConstants.Weight.getWeightEntries(14))
    ) {
        every { isDateInvValidRangeUseCaseUseCase(date = MockConstants.getDate()) } returns dateInRange
        every { checkIfWeightIsValidUseCase(value = MockConstants.Weight.VALUE) } returns valueValid
        coEvery { userRepository.getUser(userId = MockConstants.USER_ID_1) } returns Resource.Success(
            data = if (userExists) MockConstants.getUser() else null
        )
        coEvery { userRepository.removeWeightEntries(userId = MockConstants.USER_ID_1, date = MockConstants.getDate()) } returns removalResource
        coEvery { userRepository.addWeightEntry(weightEntry = any()) } returns weightEntryResource
        coEvery { userRepository.getWeightEntries(userId = MockConstants.USER_ID_1, limit = 14) } returns weightEntriesResource
        coEvery { calculateWeightProgressUseCase(userId = MockConstants.USER_ID_1, weightEntries = any()) } returns progress
    }

    private suspend fun callTestedMethod(
        value: Double = MockConstants.Weight.VALUE,
        date: LocalDate = MockConstants.getDate()
    ) = addWeightEntryUseCase(
        userId = MockConstants.USER_ID_1,
        value = value,
        date = date,
    )
}