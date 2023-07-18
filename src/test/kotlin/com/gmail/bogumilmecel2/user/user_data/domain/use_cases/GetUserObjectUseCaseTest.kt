package com.gmail.bogumilmecel2.user.user_data.domain.use_cases

import com.gmail.bogumilmecel2.MockConstants.USER_ID_1
import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class GetUserObjectUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val getUserObjectUseCase = GetUserObjectUseCase(userRepository = userRepository)

    @Test
    fun `check if user id is empty, resource error is returned`() = runTest {
        assertIs<Resource.Error<User?>>(getUserObjectUseCase(userId = ""))
    }

    @Test
    fun `check if user id is correct, resource success is returned`() = runTest {
        coEvery { userRepository.getUser(USER_ID_1) } returns Resource.Success(data = null)
        assertIs<Resource.Success<User?>>(getUserObjectUseCase(userId = USER_ID_1))
    }
}