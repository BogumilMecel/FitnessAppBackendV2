package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toLongOrZero
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetUserProductsUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(
        userId: String,
        latestTimestamp: String?
    ): Resource<List<Product>> {
        return diaryRepository.getUserProducts(
            userId = userId,
            latestTimestamp = latestTimestamp.toLongOrZero()
        )
    }
}