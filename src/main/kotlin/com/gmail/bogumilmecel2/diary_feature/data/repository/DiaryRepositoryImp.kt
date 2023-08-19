package com.gmail.bogumilmecel2.diary_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.CaloriesSumResponse
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.*
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.*
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class DiaryRepositoryImp(
    private val productDiaryCol: MongoCollection<ProductDiaryEntryDto>,
    private val recipeDiaryCol: MongoCollection<RecipeDiaryEntryDto>,
    private val productCol: MongoCollection<ProductDto>,
    private val recipeCol: MongoCollection<RecipeDto>
) : DiaryRepository, BaseRepository() {

    override suspend fun insertProductDiaryEntry(
        productDiaryEntry: ProductDiaryEntry,
        userId: String
    ): Resource<Unit> {
        return handleRequest {
            productDiaryCol.insertOne(
                productDiaryEntry.toDto(
                    userId = userId,
                    currentUtcTimestamp = productDiaryEntry.utcTimestamp
                )
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Unit> {
        return handleRequest {
            recipeDiaryCol.insertOne(
                recipeDiaryEntry.toDto(
                    userId = userId,
                    currentUtcTimestamp = recipeDiaryEntry.utcTimestamp
                )
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun getProductDiaryEntries(date: String, userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol.find(
                filter = and(
                    eq(ProductDiaryEntryDto::userId.name, userId),
                    eq(ProductDiaryEntryDto::date.name, date)
                ),
            ).toList().map {
                it.toDiaryEntry()
            }
        }
    }

    override suspend fun getRecipeDiaryEntries(date: String, userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol.find(
                filter = and(
                    eq(RecipeDiaryEntryDto::userId.name, userId),
                    eq(RecipeDiaryEntryDto::date.name, date)
                ),
            ).toList().map {
                it.toObject()
            }
        }
    }

    override suspend fun getProductDiaryEntry(id: String): Resource<ProductDiaryEntry?> {
        return handleRequest {
            productDiaryCol
                .find(eq(ProductDiaryEntryDto::_id.name, id.toObjectId()))
                .firstOrNull()
                ?.toDiaryEntry()
        }
    }

    override suspend fun getProductDiaryHistory(
        userId: String,
        skip: Int,
        limit: Int,
        searchText: String?
    ): Resource<List<ProductDiaryHistoryItem>> {
        return handleRequest {
            productDiaryCol
                .aggregate<ProductDiaryEntryDto>(
                    pipeline = buildList {
                        if (searchText != null) {
                            add(Aggregates.match(regex("productName", searchText, "i")))
                        }
                        addAll(
                            listOf(
                                Aggregates.match(eq("userId", userId)),
                                Aggregates.sort(Sorts.descending("utcTimestamp")),
                                Aggregates.skip(skip),
                                Aggregates.limit(limit),
                                Aggregates.group("\$productId", Accumulators.first("entry", "\$\$ROOT")),
                                Aggregates.replaceRoot("\$entry"),
                            )
                        )
                    }
                ).toList().map {
                    it.toProductDiarySearchItem()
                }
        }
    }

    override suspend fun getRecipeDiaryEntry(id: String): Resource<RecipeDiaryEntry?> {
        return handleRequest {
            recipeDiaryCol.find(eq(RecipeDiaryEntryDto::_id.name, id.toObjectId())).firstOrNull()?.toObject()
        }
    }

    override suspend fun editRecipeDiaryEntry(
        newNutritionValues: NutritionValues,
        newServings: Int,
        recipeDiaryEntryId: String,
        userId: String,
    ): Resource<Unit> {
        return handleRequest {
            recipeDiaryCol.updateOne(
                filter = and(
                    eq(RecipeDiaryEntryDto::_id.name, recipeDiaryEntryId.toObjectId()),
                    eq(RecipeDiaryEntryDto::userId.name, userId),
                ),
                update = Updates.combine(
                    Updates.set(RecipeDiaryEntryDto::nutritionValues.name, newNutritionValues),
                    Updates.set(RecipeDiaryEntryDto::servings.name, newServings)
                )
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun getProducts(text: String): Resource<List<Product>> {
        return handleRequest {
            productCol
                .find(regex(ProductDto::name.name, text, "i"))
                .toList()
                .map { it.toProduct() }
        }
    }

    override suspend fun getProduct(productId: String): Resource<Product?> {
        return handleRequest {
            productCol.find(eq(ProductDto::_id.name, productId.toObjectId())).firstOrNull()?.toProduct()
        }
    }

    override suspend fun editProductDiaryEntry(
        productDiaryEntryId: String,
        newWeight: Int,
        newNutritionValues: NutritionValues,
        userId: String
    ): Resource<Unit> {
        return handleRequest {
            productDiaryCol.updateOne(
                filter = and(
                    eq(ProductDiaryEntryDto::_id.name, productDiaryEntryId.toObjectId()),
                    eq(ProductDiaryEntryDto::userId.name, userId)
                ),
                update = Updates.combine(
                    Updates.set(ProductDiaryEntryDto::weight.name, newWeight),
                    Updates.set(ProductDiaryEntryDto::nutritionValues.name, newNutritionValues)
                )
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun insertProduct(product: Product, userId: String, country: Country): Resource<Product> {
        return handleRequest {
            product.copy(
                id = product.toDto(userId = userId, country = country)
                    .apply { productCol.insertOne(this) }._id.toString()
            )
        }
    }

    override suspend fun deleteProductDiaryEntry(productDiaryEntryId: String, userId: String): Resource<Boolean> {
        return handleRequest {
            productDiaryCol.deleteOne(
                and(
                    eq(ProductDiaryEntryDto::_id.name, productDiaryEntryId.toObjectId()),
                    eq(ProductDiaryEntryDto::userId.name, userId)
                )
            ).wasAcknowledged()
        }
    }

    override suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Boolean> {
        return handleRequest {
            recipeDiaryCol.deleteOne(
                and(
                    eq(ProductDiaryEntryDto::_id.name, recipeDiaryEntryId.toObjectId()),
                    eq(ProductDiaryEntryDto::userId.name, userId)
                )
            ).wasAcknowledged()
        }
    }

    override suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?> {
        return handleRequest {
            productCol.find(eq(ProductDto::barcode.name, barcode)).firstOrNull()?.toProduct()
        }
    }

    override suspend fun getUserCaloriesSum(date: String, userId: String): Resource<CaloriesSumResponse> {
        return handleRequest {
            CaloriesSumResponse(
                caloriesSum = productDiaryCol.find(
                    and(
                        eq(ProductDiaryEntryDto::date.name, date),
                        eq(ProductDiaryEntryDto::userId.name, userId)
                    )

                )
                    .toList()
                    .sumOf { it.nutritionValues.calories })
        }
    }

    override suspend fun addNewRecipe(recipe: Recipe): Resource<Recipe> {
        return handleRequest {
            recipe.copy(
                id = recipe.toDto().apply { recipeCol.insertOne(this) }._id.toString()
            )
        }
    }

    override suspend fun getRecipe(recipeId: String): Resource<Recipe?> {
        return handleRequest {
            recipeCol.find(eq(RecipeDto::_id.name, recipeId.toObjectId())).firstOrNull()?.toObject()
        }
    }

    override suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol
                .find(
                    regex(
                        RecipeDto::name.name,
                        searchText,
                        "i"
                    )
                )
                .toList()
                .map { it.toObject() }
        }
    }

    override suspend fun getUserProducts(userId: String): Resource<List<Product>> {
        return handleRequest {
            productCol
                .find(eq(ProductDto::userId.name, userId))
                .limit(50)
                .sort(Sorts.descending(ProductDto::utcTimestamp.name))
                .toList()
                .map { it.toProduct() }
        }
    }

    override suspend fun getUserRecipes(userId: String): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol
                .find(eq(RecipeDto::userId.name, userId))
                .limit(50)
                .sort(Sorts.descending(RecipeDto::utcTimestamp.name))
                .toList()
                .map { it.toObject() }
        }
    }

    override suspend fun getProductDiaryEntries(userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol
                .find(eq(ProductDiaryEntryDto::userId.name, userId))
                .toList()
                .map { it.toDiaryEntry() }
        }
    }

    override suspend fun getRecipeDiaryEntries(userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol
                .find(eq(RecipeDiaryEntryDto::userId.name, userId))
                .toList()
                .map { it.toObject() }
        }
    }

    override suspend fun getProductDiaryEntries(latestProductDiaryEntryTimestamp: Long, userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol
                .find(
                    and(
                        eq(ProductDiaryEntryDto::userId.name, userId),
                        gt(ProductDiaryEntryDto::lastEditedUtcTimestamp.name, latestProductDiaryEntryTimestamp)
                    )
                )
                .toList()
                .map { it.toDiaryEntry() }
        }
    }

    override suspend fun getRecipeDiaryEntries(latestRecipeDiaryEntryTimestamp: Long, userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol
                .find(
                    and(
                        eq(RecipeDiaryEntryDto::userId.name, userId),
                        gt(RecipeDiaryEntryDto::lastEditedUtcTimestamp.name, latestRecipeDiaryEntryTimestamp)
                    )
                )
                .toList()
                .map { it.toObject() }
        }
    }
}