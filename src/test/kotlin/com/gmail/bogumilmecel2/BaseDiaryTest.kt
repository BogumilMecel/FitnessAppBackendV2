package com.gmail.bogumilmecel2

import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import io.mockk.mockk

open class BaseDiaryTest: BaseTest() {
    val diaryRepository = mockk<DiaryRepository>()
}