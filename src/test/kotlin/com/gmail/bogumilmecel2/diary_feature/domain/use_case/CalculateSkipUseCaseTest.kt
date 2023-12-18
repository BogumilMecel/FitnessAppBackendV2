package com.gmail.bogumilmecel2.diary_feature.domain.use_case

import com.gmail.bogumilmecel2.common.domain.constants.Constants.DEFAULT_PAGE_SIZE
import org.junit.Test
import kotlin.test.assertEquals

class CalculateSkipUseCaseTest {
    private val calculateSkipUseCase = CalculateSkipUseCase()

    @Test
    fun `Check if correct value is returned`() {
        testMethod(page = 1, expectedValue = DEFAULT_PAGE_SIZE * 0)
    }

    @Test
    fun `Check if correct value is returned 2`() {
        testMethod(page = 10, expectedValue = DEFAULT_PAGE_SIZE * 9)
    }

    private fun testMethod(
        page: Int,
        expectedValue: Int
    ) {
        assertEquals(
            expected = expectedValue,
            actual = calculateSkipUseCase(
                page = page,
                sizePerPage = DEFAULT_PAGE_SIZE
            )
        )
    }
}