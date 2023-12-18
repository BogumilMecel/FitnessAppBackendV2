package com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary

import com.gmail.bogumilmecel2.BaseDiaryTest
import com.gmail.bogumilmecel2.MockConstants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDate
import com.gmail.bogumilmecel2.common.util.extensions.toLocalDateTime
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDateInValidRangeUseCaseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class EditProductDiaryEntryUseCaseTest : BaseDiaryTest() {

    private val getProductDiaryEntryUseCase = mockkClass(GetProductDiaryEntryUseCase::class)
    private val isDateInValidRangeUseCaseUseCase = mockkClass(IsDateInValidRangeUseCaseUseCase::class)
    private val editProductDiaryEntryUseCase = EditProductDiaryEntryUseCase(
        diaryRepository = diaryRepository,
        getProductDiaryEntryUseCase = getProductDiaryEntryUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
    )

    @Test
    fun `Check if getProductDiaryEntry returns resource error, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Error())
        assertIs<Resource.Error<ProductDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if getProductDiaryEntry returns null, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Success(data = null))
        assertIs<Resource.Error<ProductDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if user id do not match, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<ProductDiaryEntry>>(
            callTestedMethod(
                userId = MockConstants.USER_ID_2
            )
        )
    }

    @Test
    fun `Check if request weight is the same as entry weight, resource error is returned`() = runTest {
        mockData()
        assertIs<Resource.Error<ProductDiaryEntry>>(
            callTestedMethod(
                weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_1
            )
        )
    }

    @Test
    fun `Check if entry date is not in valid range, resource error is returned`() = runTest {
        mockData(productDiaryEntryResource = Resource.Success(data = mockProductDiaryEntry(date = null)))
        assertIs<Resource.Error<ProductDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if entry date is null, resource error is returned`() = runTest {
        mockData(isDateInValidRange = false)
        assertIs<Resource.Error<ProductDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if called with correct data and repository returns resource error, resource error is returned`() = runTest {
        mockData(repositoryResource = Resource.Error())
        assertIs<Resource.Error<ProductDiaryEntry>>(callTestedMethod())
    }

    @Test
    fun `Check if called with correct data and repository returns resource success, resource success is returned`() = runTest {
        mockData()
        mockDateTime(dateTime = MockConstants.DATE_TIME_ONE_WEEK_LATER.toLocalDateTime()!!)
        val resource = callTestedMethod()
        assertIs<Resource.Success<ProductDiaryEntry>>(resource)
        val expectedProductDiaryEntry = mockProductDiaryEntry().copy(
            weight = MockConstants.Diary.CORRECT_PRODUCT_DIARY_ENTRY_WEIGHT_2,
            userId = MockConstants.USER_ID_1,
            changeDateTime = MockConstants.DATE_TIME_ONE_WEEK_LATER.toLocalDateTime()
        )
        assertEquals(
            actual = resource.data,
            expected = expectedProductDiaryEntry
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
        isDateInValidRange: Boolean = true
    ) {
        coEvery { getProductDiaryEntryUseCase(MockConstants.Diary.DIARY_ENTRY_ID_21) } returns productDiaryEntryResource
        coEvery { diaryRepository.editProductDiaryEntry(productDiaryEntry = any(), userId = MockConstants.USER_ID_1) } returns repositoryResource
        coEvery { isDateInValidRangeUseCaseUseCase(date = MockConstants.DATE.toLocalDate()!!) } returns isDateInValidRange
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
        userId: String = MockConstants.USER_ID_1,
        date: LocalDate? = MockConstants.DATE.toLocalDate()
    ) = ProductDiaryEntry(
        id = MockConstants.Diary.DIARY_ENTRY_ID_21,
        productId = MockConstants.Diary.PRODUCT_ID_11,
        weight = weight,
        userId = userId,
        date = date,
        changeDateTime = MockConstants.DATE_TIME.toLocalDateTime()
    )
}