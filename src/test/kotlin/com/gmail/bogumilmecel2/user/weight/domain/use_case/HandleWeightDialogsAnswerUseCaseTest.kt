package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogs
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HandleWeightDialogsAnswerUseCaseTest : BaseTest() {

    private val userRepository = mockk<UserRepository>()
    private val handleWeightDialogsAnswerUseCase = HandleWeightDialogsAnswerUseCase(userRepository)

    @Test
    fun `Check if user repository returns user resource error, resource error is returned`() = runTest {
        mockData(userResource = Resource.Error())
        assertIs<Resource.Error<WeightDialogs>>(callTestedMethod())
    }

    @Test
    fun `Check if user repository returns null user, resource error is returned`() = runTest {
        mockData(userResource = Resource.Success(data = null))
        assertIs<Resource.Error<WeightDialogs>>(callTestedMethod())
    }

    @Test
    fun `Check if user repository returns resource error when updating, resource error is returned`() = runTest {
        mockData(updateResource = Resource.Error())
        assertIs<Resource.Error<WeightDialogs>>(callTestedMethod())
    }

    @Test
    fun `Check if user repository returns resource success correct askedCount is used and resource success is returned`() = runTest {
        val expectedWeightDialogs = WeightDialogs(
            askedCount = 2,
            accepted = false,
            lastTimeAsked = MockConstants.MOCK_DATE_2022
        )
        with(expectedWeightDialogs) {
            mockLocalDate(MockConstants.MOCK_DATE_2022)
            mockData(
                userResource = Resource.Success(mockUser(accepted = accepted, askedCount = askedCount)),
                updateResource = Resource.Success(Unit)
            )
            val resource = callTestedMethod(accepted = accepted)
            assertIs<Resource.Success<WeightDialogs>>(resource)
            val expectedAskedCount = askedCount + 1
            assertEquals(
                expected = this.copy(askedCount = expectedAskedCount),
                actual = resource.data
            )
            coVerify(exactly = 1) {
                userRepository.updateWeightDialogsData(
                    weightDialogs = this@with.copy(askedCount = expectedAskedCount),
                    userId = MockConstants.USER_ID
                )
            }
        }
    }

    private fun mockData(
        userResource: Resource<User?> = Resource.Success(data = mockUser()),
        updateResource: Resource<Unit> = Resource.Success(Unit),
    ) {
        coEvery { userRepository.getUser(MockConstants.USER_ID) } returns userResource
        coEvery {
            userRepository.updateWeightDialogsData(
                weightDialogs = any(),
                userId = MockConstants.USER_ID
            )
        } returns updateResource
    }

    private fun mockUser(
        accepted: Boolean? = true,
        lastTimeAsked: String = MockConstants.MOCK_DATE_2021,
        askedCount: Int = 1,
    ) = User(
        weightDialogs = WeightDialogs(
            accepted = accepted,
            lastTimeAsked = lastTimeAsked,
            askedCount = askedCount
        )
    )

    private suspend fun callTestedMethod(accepted: Boolean? = true) = handleWeightDialogsAnswerUseCase(
        userId = MockConstants.USER_ID,
        weightDialogsRequest = WeightDialogsRequest(accepted = accepted),
        timeZone = timeZone
    )
}