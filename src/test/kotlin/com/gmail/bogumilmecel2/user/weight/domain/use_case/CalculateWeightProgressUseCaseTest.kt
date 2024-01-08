package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.BaseTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CalculateWeightProgressUseCaseTest : BaseTest() {

    private val userRepository = mockk<UserRepository>()
    private val calculateWeightProgressUseCase = CalculateWeightProgressUseCase(userRepository)

    @Test
    fun `Check if null is returned when entries are empty`() = runTest {
        assertNull(
            callTestedMethod(
                weightEntries = listOf()
            )
        )
    }

    @Test
    fun `Check if null is returned when there is only 1 entry`() = runTest {
        assertNull(
            callTestedMethod(
                weightEntries = listOf( MockConstants.Weight.getWeightEntry())
            )
        )
    }

    @Test
    fun `Check if correct value is returned`() = runTest {
        assertEquals(
            expected = 50.0,
            actual = callTestedMethod(
                weightEntries = listOf(
                    MockConstants.Weight.getWeightEntry(
                        value = 100.0,
                        creationDateTime = MockConstants.getDateTime()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 150.0,
                        creationDateTime = MockConstants.getDateTimeOneWeekLater()
                    )
                )
            ).also {
                verifyUpdateWeightData(expectedWeightProgress = it!!)
            }
        )
    }

    @Test
    fun `Check if correct value is returned 2`() = runTest {
        assertEquals(
            expected = -50.0,
            actual = callTestedMethod(
                weightEntries = listOf(
                    MockConstants.Weight.getWeightEntry(
                        value = 150.0,
                        creationDateTime = MockConstants.getDateTime()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 100.0,
                        creationDateTime = MockConstants.getDateTimeOneWeekLater()
                    ),
                )
            ).also {
                verifyUpdateWeightData(expectedWeightProgress = it!!)
            }
        )
    }

    @Test
    fun `Check if correct value is returned 3`() = runTest {
        assertEquals(
            expected = 100.0,
            actual = callTestedMethod(
                weightEntries = listOf(
                    MockConstants.Weight.getWeightEntry(
                        value = 100.0,
                        creationDateTime = MockConstants.getDateTime()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 150.0,
                        creationDateTime = MockConstants.getDateTimeOneWeekLater()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 200.0,
                        creationDateTime = MockConstants.getDateTimeTwoWeeksLater()
                    )
                )
            ).also {
                verifyUpdateWeightData(expectedWeightProgress = it!!)
            }
        )
    }

    @Test
    fun `Check if correct value is returned 4`() = runTest {
        assertEquals(
            expected = -100.0,
            actual = callTestedMethod(
                weightEntries = listOf(
                    MockConstants.Weight.getWeightEntry(
                        value = 100.0,
                        creationDateTime = MockConstants.getDateTimeTwoWeeksLater()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 150.0,
                        creationDateTime = MockConstants.getDateTimeOneWeekLater()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 200.0,
                        creationDateTime = MockConstants.getDateTime()
                    )
                )
            ).also {
                verifyUpdateWeightData(expectedWeightProgress = it!!)
            }
        )
    }

    @Test
    fun `Check if correct value is returned 5`() = runTest {
        assertEquals(
            expected = 0.0,
            actual = callTestedMethod(
                weightEntries = listOf(
                    MockConstants.Weight.getWeightEntry(
                        value = 100.0,
                        creationDateTime = MockConstants.getDateTimeTwoWeeksLater()
                    ),
                    MockConstants.Weight.getWeightEntry(
                        value = 100.0,
                        creationDateTime = MockConstants.getDateTimeOneWeekLater()
                    ),
                )
            ).also {
                verifyUpdateWeightData(expectedWeightProgress = it!!)
            }
        )
    }

    private fun verifyUpdateWeightData(
        exactly: Int = 1,
        expectedWeightProgress: Double
    ) {
        coVerify(exactly = exactly) {
            userRepository.updateWeightProgress(
                userId = MockConstants.USER_ID_1,
                weightProgress = expectedWeightProgress
            )
        }
    }

    private suspend fun callTestedMethod(weightEntries: List<WeightEntryDto>): Double? {
        coEvery {
            userRepository.updateWeightProgress(
                userId = MockConstants.USER_ID_1,
                weightProgress = any()
            )
        } returns Resource.Success(Unit)

        return calculateWeightProgressUseCase(
            userId = MockConstants.USER_ID_1,
            weightEntries = weightEntries
        )
    }
}