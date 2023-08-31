package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsTimestampInTwoWeeksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class EditProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getProductDiaryEntryUseCase = mockkClass(GetProductDiaryEntryUseCase::class)
    private val editProductDiaryEntryUseCase = EditProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getProductDiaryEntryUseCase = getProductDiaryEntryUseCase,
        isTimestampInTwoWeeksUseCase = IsTimestampInTwoWeeksUseCase()
    )

    @Test
    fun `Check if getProductDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if getProductDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if user id do not match, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                userId = MockConstants.USER_ID_2
            )
        )
    }

    @Test
    fun `Check if request weight is the same as entry weight, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<Unit>>(
            callTestedMethod(
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        )
    }

    @Test
    fun `Check if entry is older than 2 weeks, resource error is returned`() = runTest {
        mockLocalDate(utcTimestamp = MockConstants.TIMESTAMP_MORE_THAN_2_LATER)
        mockData()
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if called with correct data and repository returns resource error, resource error is returned`() = runTest {
        mockData(repositoryResource = Resource.Error())
        assertIs<Resource.Error<Unit>>(callTestedMethod())
    }

    @Test
    fun `Check if called with correct data and repository returns resource success, resource success is returned`() = runTest {
        mockData()
        mockLocalDate(utcTimestamp = MockConstants.TIMESTAMP_1_WEEKS_LATER)
        assertIs<Resource.Success<Unit>>(
            callTestedMethod()
        )
        val expectedProductDiaryEntry = mockProductDiaryEntry().copy(
            weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2,
            userId = MockConstants.USER_ID_1,
            lastEditedUtcTimestamp = MockConstants.TIMESTAMP_1_WEEKS_LATER
        )
        coVerify(exactly = 1) {
            diaryRepository.editProductDiaryEntry(
                productDiaryEntry = expectedProductDiaryEntry,
                userId = MockConstants.USER_ID_1
            )
        }
    }

    private fun mockData(
        productDiaryEntryResource: Resource<ProductDiaryEntry?> = Resource.Success(mockProductDiaryEntry()),
        repositoryResource: Resource<Unit> = Resource.Success(Unit),
    ) {
        coEvery { getProductDiaryEntryUseCase(MockConstants.Diary.DIARY_ENTRY_ID_21) } returns productDiaryEntryResource
        coEvery { diaryRepository.editProductDiaryEntry(productDiaryEntry = any(), userId = MockConstants.USER_ID_1) } returns repositoryResource
    }

    private suspend fun callTestedMethod(
        weight: Int = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2,
        userId: String = MockConstants.USER_ID_1
    ) = editProductDiaryEntryUseCase(
        productDiaryEntry = mockProductDiaryEntry(
            weight = weight,
            userId = userId
        ),
        userId = userId
    )

    private fun mockProductDiaryEntry(
        weight: Int = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1,
        userId: String = MockConstants.USER_ID_1
    ) = ProductDiaryEntry(
        id = MockConstants.Diary.DIARY_ENTRY_ID_21,
        productId = MockConstants.Diary.PRODUCT_ID_11,
        weight = weight,
        utcTimestamp = MockConstants.TIMESTAMP,
        userId = userId
    )
}