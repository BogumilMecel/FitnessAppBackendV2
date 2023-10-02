package com.gmail.bogumilmecel2.diary_feature.domain.use_case

class CalculateSkipUseCase {
    operator fun invoke(page: Int, sizePerPage: Int) = sizePerPage * (page - 1)
}