package com.gmail.bogumilmecel2.diary_feature.domain.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry

interface DiaryRepository {
    suspend fun getProductDiaryEntries(date: String, userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(date: String, userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntries(latestProductDiaryEntryTimestamp: Long, userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(latestRecipeDiaryEntryTimestamp: Long, userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntries(userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntry(id: String): Resource<ProductDiaryEntry?>
    suspend fun getRecipeDiaryEntry(id: String): Resource<RecipeDiaryEntry?>
    suspend fun insertProductDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<ProductDiaryEntry>
    suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<RecipeDiaryEntry>
    suspend fun getProducts(
        text: String,
        skip: Int
    ): Resource<List<Product>>
    suspend fun getProduct(productId: String): Resource<Product?>
    suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Boolean>
    suspend fun editProductDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<Unit>
    suspend fun editRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Unit>
    suspend fun insertProduct(product: Product, userId: String, country: Country): Resource<Product>
    suspend fun deleteProductDiaryEntry(productDiaryEntryId: String, userId: String): Resource<Boolean>
    suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?>
    suspend fun addNewRecipe(recipe: Recipe): Resource<Recipe>
    suspend fun getRecipe(recipeId: String): Resource<Recipe?>
    suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>>
    suspend fun getProductDiaryHistory(
        userId: String,
        skip: Int,
        limit: Int,
        searchText: String?
    ): Resource<List<ProductDiaryHistoryItem>>
    suspend fun getUserProducts(
        userId: String,
        latestTimestamp: Long
    ): Resource<List<Product>>
    suspend fun getUserRecipes(
        userId: String,
        latestTimestamp: Long
    ): Resource<List<Recipe>>
}