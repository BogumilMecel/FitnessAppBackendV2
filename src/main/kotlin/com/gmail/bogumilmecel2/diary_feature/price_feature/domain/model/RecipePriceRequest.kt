package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import kotlinx.serialization.Serializable

@Serializable
data class RecipePriceRequest(
    val ingredients: List<Ingredient>
)
