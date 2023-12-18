package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.util.CustomDateUtils.getOrMinDateTime
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import kotlinx.datetime.LocalDateTime

class GetUserProductsUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestDateTime: LocalDateTime?
    ): Resource<List<Product>> {
        return diaryRepository.getUserProducts(
            userId = userId,
            latestDateTime = latestDateTime.getOrMinDateTime()
        )
    }
}