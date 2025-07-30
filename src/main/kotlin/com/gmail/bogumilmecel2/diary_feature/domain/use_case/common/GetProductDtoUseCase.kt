package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class GetProductDtoUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(productId: String): Resource<ProductDto?> {
        return diaryRepository.getProduct(productId = productId)
    }
}