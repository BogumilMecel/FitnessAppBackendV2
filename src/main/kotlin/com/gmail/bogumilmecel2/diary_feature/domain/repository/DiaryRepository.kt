package com.gmail.bogumilmecel2.diary_feature.domain.repository

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.CaloriesSumResponse
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.price.Price
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.Recipe
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto

interface DiaryRepository {
    suspend fun getProductDiaryEntries(date: String, userId: String): Resource<List<ProductDiaryEntryDto>>
    suspend fun getRecipeDiaryEntries(date: String, userId: String): Resource<List<RecipeDiaryEntryDto>>
    suspend fun getDiaryEntry(id: String): Resource<ProductDiaryEntry?>
    suspend fun insertDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<ProductDiaryEntryDto>
    suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Boolean>
    suspend fun getProducts(text: String): Resource<List<Product>>
    suspend fun getProductHistory(userId: String): Resource<List<Product>>
    suspend fun getProduct(productId: String): Resource<Product?>
    suspend fun removeDiaryEntry(diaryEntryId: String): Resource<Boolean>
    suspend fun editDiaryEntry(productDiaryEntry: ProductDiaryEntry): Resource<Boolean>
    suspend fun insertProduct(product: Product, userId: String): Resource<Product>
    suspend fun deleteDiaryEntry(diaryEntryId: String, userId: String): Resource<Boolean>
    suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?>
    suspend fun getUserCaloriesSum(date: String, userId: String): Resource<CaloriesSumResponse>
    suspend fun addNewPrice(productId: String, price: Price): Resource<Price>
    suspend fun addNewRecipe(recipe: Recipe): Resource<Recipe>
    suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>>
}