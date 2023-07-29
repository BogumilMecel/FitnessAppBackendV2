package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import kotlinx.serialization.Serializable

@Serializable
data class UserDiaryItemsResponse(
    val userProducts: List<Product>,
    val userRecipes: List<Recipe>
)