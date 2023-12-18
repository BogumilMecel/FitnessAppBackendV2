package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsQuestion
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class HandleWeightDialogsQuestionAnswerUseCaseTest : BaseTest() {

    private val userRepository = mockk<UserRepository>()
    private val handleWeightDialogsAnswerUseCase = HandleWeightDialogsAnswerUseCase(userRepository)

    @Test
    fun `Check if get weight dialogs returns resource error, resource error is returned`() = runTest {
        mockData(weightDialogsQuestionsResource = Resource.Error())
        callTestedMethod().assertIsError()
    }

    @Test
    fun `Check if accepted is null and user has been already asked 3 times, resource error is returned`() = runTest {
        mockData(weightDialogsQuestionsResource = Resource.Success(data = MockConstants.Weight.getWeightDialogsQuestions(count = 4)))
        callTestedMethod(accepted = null).assertIsError()
    }

    @Test
    fun `Check if accepted is null and user has not been already asked 3 times and inserting question returns resource error, resource error is returned`() =
        runTest {
            mockData(insertWeightDialogsQuestionResource = Resource.Error())
            callTestedMethod(accepted = null).assertIsError()
        }

    @Test
    fun `Check if accepted is null and user has not been already asked 3 times and inserting question returns resource success, resource success is returned`() =
        runTest {
            mockData()
            callTestedMethod(accepted = null).assertIsSuccess()
        }

    @Test
    fun `Check if repository returns resource error when updating, resource error is returned`() = runTest {
        mockData(updateResource = Resource.Error())
        assertIs<Resource.Error<WeightDialogsQuestion>>(callTestedMethod())
    }

    @Test
    fun `Check if user repository returns resource success, resource success is returned`() = runTest {
        val accepted = true
        mockData()
        callTestedMethod(accepted = accepted).assertIsSuccess()
        coVerify(exactly = 1) {
            userRepository.updateAskForWeightDaily(
                accepted = accepted,
                userId = MockConstants.USER_ID_1
            )
        }
    }

    private fun mockData(
        updateResource: Resource<Unit> = Resource.Success(Unit),
        weightDialogsQuestionsResource: Resource<List<WeightDialogsQuestion>> = Resource.Success(data = MockConstants.Weight.getWeightDialogsQuestions()),
        insertWeightDialogsQuestionResource: Resource<Unit> = Resource.Success(Unit)
    ) {
        coEvery {
            userRepository.updateAskForWeightDaily(accepted = any(), userId = MockConstants.USER_ID_1)
        } returns updateResource
        coEvery { userRepository.getWeightDialogsQuestions(userId = MockConstants.USER_ID_1) } returns weightDialogsQuestionsResource
        coEvery { userRepository.insertWeightDialogsQuestion(weightDialogsQuestion = any()) } returns insertWeightDialogsQuestionResource
    }

    private suspend fun callTestedMethod(accepted: Boolean? = true) = handleWeightDialogsAnswerUseCase(
        userId = MockConstants.USER_ID_1,
        weightDialogsRequest = WeightDialogsRequest(accepted = accepted),
        timeZone = timeZone
    )
}