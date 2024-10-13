package com.gmail.bogumilmecel2.user.dates.domain.use_cases

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.dates.domain.model.AvailableDiaryDatesResponse
import com.gmail.bogumilmecel2.user.dates.utils.createAvailableDiaryDates
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetAvailableDiaryDatesUseCaseTest : BaseTest() {

    val userRepository = mockk<UserRepository>()
    val createAvailableDiaryDatesUseCase = mockkClass(CreateAvailableDiaryDatesUseCase::class)
    val getAvailableDiaryDatesUseCase = GetAvailableDiaryDatesUseCase(
        userRepository = userRepository,
        createAvailableDiaryDatesUseCase = createAvailableDiaryDatesUseCase,
    )

    @Test
    fun `Check if user repository returns error resource error is returned`() = runTest {
        initializeMocks(withUserError = true)
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if user repository returns null resource error is returned`() = runTest {
        initializeMocks(user = null)
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if create dates returns resource error resource error is returned`() = runTest {
        initializeMocks(withCreateAvailableDiaryDatesError = true)
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if create dates returns dates resource success is returned with correct data`() = runTest {
        initializeMocks()
        callTestedMethod().run {
            assertIsSuccess()
            assertEquals(
                expected = AvailableDiaryDatesResponse(
                    availableDates = createAvailableDiaryDates(daysCount = Constants.Diary.AVAILABLE_DIARY_DATES_COUNT)
                ),
                actual = data,
            )
        }
    }

    private fun initializeMocks(
        withUserError: Boolean = false,
        user: UserDto? = MockConstants.getUser(),
        withCreateAvailableDiaryDatesError: Boolean = false,
    ) {
        coEvery { userRepository.getUser(userId = MockConstants.USER_ID_1) } returns if (withUserError) {
            Resource.Error()
        } else {
            Resource.Success(user)
        }
        coEvery {
            createAvailableDiaryDatesUseCase(availableDaysCount = any(), timeZone = any())
        } returns if (withCreateAvailableDiaryDatesError) {
            Resource.Error()
        } else {
            Resource.Success(data = createAvailableDiaryDates(Constants.Diary.AVAILABLE_DIARY_DATES_COUNT))
        }
    }

    private suspend fun callTestedMethod() = getAvailableDiaryDatesUseCase(
        userId = MockConstants.USER_ID_1,
        timeZone = TimeZone.UTC,
    )
}