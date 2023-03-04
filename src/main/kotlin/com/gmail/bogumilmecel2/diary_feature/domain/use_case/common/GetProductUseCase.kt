package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductUseCase(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(productId: String): Resource<Product?> {
        return diaryRepository.getProduct(
            productId = productId
        )
    }
}