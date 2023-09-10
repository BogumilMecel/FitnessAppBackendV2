package com.gmail.bogumilmecel2.diary_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.*
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.*
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.gt

class DiaryRepositoryImp(
    private val productDiaryCol: CoroutineCollection<ProductDiaryEntryDto>,
    private val recipeDiaryCol: CoroutineCollection<RecipeDiaryEntryDto>,
    private val productCol: CoroutineCollection<ProductDto>,
    private val recipeCol: CoroutineCollection<RecipeDto>
) : DiaryRepository, BaseRepository() {

    override suspend fun insertProductDiaryEntry(
        productDiaryEntry: ProductDiaryEntry,
        userId: String
    ): Resource<ProductDiaryEntry> {
        return handleRequest {
            productDiaryEntry.copy(
                id = productDiaryEntry.toDto(userId = userId)
                    .apply { productDiaryCol.insertOne(this) }._id.toString()
            )
        }
    }

    override suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<RecipeDiaryEntry> {
        return handleRequest {
            recipeDiaryEntry.copy(
                id = recipeDiaryEntry.toDto(userId = userId)
                    .apply { recipeDiaryCol.insertOne(this) }._id.toString()
            )
        }
    }

    override suspend fun getProductDiaryEntries(date: String, userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol.find(
                ProductDiaryEntryDto::userId eq userId,
                ProductDiaryEntryDto::date eq date
            ).toList().map {
                it.toDiaryEntry()
            }
        }
    }

    override suspend fun getRecipeDiaryEntries(date: String, userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol.find(
                RecipeDiaryEntryDto::userId eq userId,
                RecipeDiaryEntryDto::date eq date,
            ).toList().map {
                it.toObject()
            }
        }
    }

    override suspend fun getProductDiaryEntry(id: String): Resource<ProductDiaryEntry?> {
        return handleRequest {
            productDiaryCol.findOne(ProductDiaryEntryDto::_id eq id.toObjectId())?.toDiaryEntry()
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
                            add(Aggregates.match(Filters.regex("productName", searchText, "i")))
                        }
                        addAll(
                            listOf(
                                Aggregates.match(Filters.eq("userId", userId)),
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
            recipeDiaryCol.findOne(RecipeDiaryEntryDto::_id eq id.toObjectId())?.toObject()
        }
    }

    override suspend fun editRecipeDiaryEntry(
        recipeDiaryEntry: RecipeDiaryEntry,
        userId: String,
    ): Resource<Unit> {
        return handleRequest {
            recipeDiaryCol.updateOne(
                and(
                    RecipeDiaryEntryDto::_id eq recipeDiaryEntry.id.toObjectId(),
                    RecipeDiaryEntryDto::userId eq userId
                ),
                recipeDiaryEntry.toDto(userId = userId)
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun getProducts(text: String): Resource<List<Product>> {
        return handleRequest {
            productCol.find("{'name': {'${MongoOperator.regex}': '$text', '${MongoOperator.options}': 'i'}}")
                .toList()
                .map {
                    it.toProduct()
                }
        }
    }

    override suspend fun getProduct(productId: String): Resource<Product?> {
        return handleRequest {
            productCol.findOne(ProductDto::_id eq productId.toObjectId())?.toProduct()
        }
    }

    override suspend fun editProductDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<Unit> {
        return handleRequest {
            productDiaryCol.updateOne(
                and(
                    ProductDiaryEntryDto::_id eq productDiaryEntry.id.toObjectId(),
                    ProductDiaryEntryDto::userId eq userId
                ),
                productDiaryEntry.toDto(userId = userId)
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
                ProductDiaryEntryDto::_id eq productDiaryEntryId.toObjectId(),
                ProductDiaryEntryDto::userId eq userId
            ).wasAcknowledged()
        }
    }

    override suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Boolean> {
        return handleRequest {
            recipeDiaryCol.deleteOne(
                RecipeDiaryEntryDto::_id eq recipeDiaryEntryId.toObjectId(),
                RecipeDiaryEntryDto::userId eq userId
            ).wasAcknowledged()
        }
    }

    override suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?> {
        return handleRequest {
            productCol.findOne(ProductDto::barcode eq barcode)?.toProduct()
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
            recipeCol.findOneById(id = recipeId.toObjectId())?.toObject()
        }
    }

    override suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol.find("{'name': {'${MongoOperator.regex}': '$searchText', '${MongoOperator.options}': 'i'}}")
                .toList()
                .map {
                    it.toObject()
                }
        }
    }

    override suspend fun getUserProducts(userId: String): Resource<List<Product>> {
        return handleRequest {
            productCol
                .find(ProductDto::userId eq userId)
                .limit(50).descendingSort(ProductDto::utcTimestamp)
                .toList()
                .map { it.toProduct() }
        }
    }

    override suspend fun getUserRecipes(userId: String): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol
                .find(RecipeDto::userId eq userId)
                .limit(50).descendingSort(RecipeDto::utcTimestamp)
                .toList()
                .map { it.toObject() }
        }
    }

    override suspend fun getProductDiaryEntries(userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol
                .find(ProductDiaryEntryDto::userId eq userId)
                .toList()
                .map { it.toDiaryEntry() }
        }
    }

    override suspend fun getRecipeDiaryEntries(userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol
                .find(RecipeDiaryEntryDto::userId eq userId)
                .toList()
                .map { it.toObject() }
        }
    }

    override suspend fun getProductDiaryEntries(latestProductDiaryEntryTimestamp: Long, userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol
                .find(
                    and(
                        ProductDiaryEntryDto::userId eq userId,
                        ProductDiaryEntryDto::lastEditedUtcTimestamp gt latestProductDiaryEntryTimestamp
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
                        RecipeDiaryEntryDto::userId eq userId,
                        RecipeDiaryEntryDto::lastEditedUtcTimestamp gt latestRecipeDiaryEntryTimestamp
                    )
                )
                .toList()
                .map { it.toObject() }
        }
    }
}