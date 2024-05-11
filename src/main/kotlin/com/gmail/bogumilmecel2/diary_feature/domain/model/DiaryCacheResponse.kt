package com.gmail.bogumilmecel2.diary_feature.domain.model

import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import kotlinx.serialization.SerialName

data class DiaryCacheResponse(
    @SerialName("products")
    val products: List<Product>,

    @SerialName("product_diary_entries")
    val productDiaryEntries: List<ProductDiaryEntry>,

    @SerialName("recipes")
    val recipes: List<Recipe>,

    @SerialName("recipe_diary_entries")
    val recipeDiaryEntries: List<RecipeDiaryEntry>,
)