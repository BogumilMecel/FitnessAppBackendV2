package com.gmail.bogumilmecel2.diary_feature.domain.use_case.common

import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UnknownException

class GetProductUseCase(private val diaryRepository: DiaryRepository) {
    suspend operator fun invoke(productId: String?): Product? {
        if (productId == null) throw UnknownException

        return diaryRepository.getProduct(productId = productId).data?.toProduct()
    }
}