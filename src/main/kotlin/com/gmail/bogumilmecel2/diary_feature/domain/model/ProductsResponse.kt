package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    @SerialName("results")
    val results: List<Product>,

    @SerialName("page")
    val page: Int,

    @SerialName("has_next_page")
    val hasNextPage: Boolean,
)
