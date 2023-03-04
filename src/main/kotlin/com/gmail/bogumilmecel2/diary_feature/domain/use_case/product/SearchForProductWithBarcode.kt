package com.gmail.bogumilmecel2.diary_feature.domain.use_case.product

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository

class SearchForProductWithBarcode(
    private val diaryRepository: DiaryRepository
) {

    suspend operator fun invoke(barcode:String):Resource<Product?>{
        return diaryRepository.searchForProductWithBarcode(barcode)
    }
}