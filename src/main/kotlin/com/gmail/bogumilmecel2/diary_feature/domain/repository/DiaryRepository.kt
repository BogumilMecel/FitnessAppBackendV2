package com.gmail.bogumilmecel2.diary_feature.domain.repository

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface DiaryRepository {
    suspend fun getProductDiaryEntries(date: LocalDate, userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(date: LocalDate, userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntries(latestDateTime: LocalDateTime, userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(latestDateTime: LocalDateTime, userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntries(userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntry(id: String): Resource<ProductDiaryEntryDto?>
    suspend fun getRecipeDiaryEntry(id: String): Resource<RecipeDiaryEntryDto?>
    suspend fun insertProductDiaryEntry(productDiaryEntry: ProductDiaryEntryDto): Resource<ProductDiaryEntryDto>
    suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntryDto): Resource<RecipeDiaryEntry>
    suspend fun getProducts(
        text: String,
        skip: Int
    ): Resource<List<Product>>
    suspend fun getProduct(productId: String): Resource<ProductDto?>
    suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Unit>
    suspend fun editProductDiaryEntry(productDiaryEntry: ProductDiaryEntryDto): Resource<Unit>
    suspend fun editRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntryDto): Resource<Unit>
    suspend fun insertProduct(product: ProductDto): Resource<Product>
    suspend fun deleteProductDiaryEntry(productDiaryEntryId: String, userId: String): Resource<Unit>
    suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?>
    suspend fun insertRecipe(recipe: RecipeDto): Resource<Recipe>
    suspend fun getRecipe(recipeId: String): Resource<RecipeDto?>
    suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>>
    suspend fun getProductDiaryHistory(
        userId: String,
        skip: Int,
        limit: Int,
        searchText: String?
    ): Resource<List<ProductDiaryHistoryItem>>
    suspend fun getUserProducts(
        userId: String,
        latestDateTime: LocalDateTime?,
    ): Resource<List<Product>>
    suspend fun getUserRecipes(
        userId: String,
        latestDateTime: LocalDateTime?,
    ): Resource<List<Recipe>>
}