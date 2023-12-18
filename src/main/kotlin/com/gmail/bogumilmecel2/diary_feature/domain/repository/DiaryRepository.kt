package com.gmail.bogumilmecel2.diary_feature.domain.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.CaloriesSumResponse
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry

interface DiaryRepository {
    suspend fun getProductDiaryEntries(date: String, userId: String): Resource<List<ProductDiaryEntry>>
    suspend fun getRecipeDiaryEntries(date: String, userId: String): Resource<List<RecipeDiaryEntry>>
    suspend fun getProductDiaryEntry(id: String): Resource<ProductDiaryEntry?>
    suspend fun getRecipeDiaryEntry(id: String): Resource<RecipeDiaryEntry?>
    suspend fun insertDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<ProductDiaryEntry>
    suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Boolean>
    suspend fun getProducts(text: String): Resource<List<Product>>
    suspend fun getProduct(productId: String): Resource<Product?>
    suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Boolean>
    suspend fun editProductDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<Unit>
    suspend fun editRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Unit>
    suspend fun insertProduct(product: Product, userId: String, country: Country): Resource<Product>
    suspend fun deleteProductDiaryEntry(productDiaryEntryId: String, userId: String): Resource<Boolean>
    suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?>
    suspend fun getUserCaloriesSum(date: String, userId: String): Resource<CaloriesSumResponse>
    suspend fun addNewRecipe(recipe: Recipe): Resource<Recipe>
    suspend fun getRecipe(recipeId: String): Resource<Recipe?>
    suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>>
}