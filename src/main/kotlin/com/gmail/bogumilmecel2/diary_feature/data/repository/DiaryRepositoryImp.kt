package com.gmail.bogumilmecel2.diary_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.constants.Constants.DEFAULT_PAGE_SIZE
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toProductDiarySearchItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.*
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.gt

class DiaryRepositoryImp(
    private val productDiaryCol: CoroutineCollection<ProductDiaryEntryDto>,
    private val recipeDiaryCol: CoroutineCollection<RecipeDiaryEntryDto>,
    private val productCol: CoroutineCollection<ProductDto>,
    private val recipeCol: CoroutineCollection<RecipeDto>
) : DiaryRepository, BaseRepository() {

    override suspend fun insertProductDiaryEntry(productDiaryEntry: ProductDiaryEntryDto): Resource<ProductDiaryEntryDto> {
        return handleRequest {
            productDiaryEntry.apply { productDiaryCol.insertOne(this) }
        }
    }

    override suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntryDto, userId: String): Resource<RecipeDiaryEntry> {
        return handleRequest {
            recipeDiaryEntry.apply { recipeDiaryCol.insertOne(this) }.toRecipeDiaryEntry()
        }
    }

    override suspend fun getProductDiaryEntries(date: LocalDate, userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol.find(
                ProductDiaryEntryDto::userId eq userId,
                ProductDiaryEntryDto::date eq date.toString()
            ).toList().map {
                it.toProductDiaryEntry()
            }
        }
    }

    override suspend fun getRecipeDiaryEntries(date: LocalDate, userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol.find(
                RecipeDiaryEntryDto::userId eq userId,
                RecipeDiaryEntryDto::date eq date.toString(),
            ).toList().map {
                it.toRecipeDiaryEntry()
            }
        }
    }

    override suspend fun getProductDiaryEntry(id: String): Resource<ProductDiaryEntryDto?> {
        return handleRequest {
            productDiaryCol.findOne(ProductDiaryEntryDto::_id eq id.toObjectId())
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

    override suspend fun getRecipeDiaryEntry(id: String): Resource<RecipeDiaryEntryDto?> {
        return handleRequest {
            recipeDiaryCol.findOne(RecipeDiaryEntryDto::_id eq id.toObjectId())
        }
    }

    override suspend fun editRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntryDto): Resource<Unit> {
        return handleRequest {
            recipeDiaryCol.updateOne(
                RecipeDiaryEntryDto::_id eq recipeDiaryEntry._id,
                recipeDiaryEntry
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun getProducts(text: String, skip: Int): Resource<List<Product>> {
        return handleRequest {
            productCol.find("{'name': {'${MongoOperator.regex}': '$text', '${MongoOperator.options}': 'i'}}")
                .limit(limit = DEFAULT_PAGE_SIZE)
                .skip(skip = skip)
                .toList()
                .map {
                    it.toProduct()
                }
        }
    }

    override suspend fun getProduct(productId: String): Resource<ProductDto?> {
        return handleRequest {
            productCol.findOne(ProductDto::_id eq productId.toObjectId())
        }
    }

    override suspend fun editProductDiaryEntry(productDiaryEntry: ProductDiaryEntryDto): Resource<Unit> {
        return handleRequest {
            productDiaryCol.updateOne(
                ProductDiaryEntryDto::_id eq productDiaryEntry._id,
                productDiaryEntry
            ).wasAcknowledgedOrThrow()
        }
    }

    override suspend fun insertProduct(product: ProductDto): Resource<Product> {
        return handleRequest {
            product.apply { productCol.insertOne(this) }.toProduct()
        }
    }

    override suspend fun deleteProductDiaryEntry(productDiaryEntryId: String, userId: String): Resource<Unit> {
        return handleRequest {
            productDiaryCol.deleteOne(
                ProductDiaryEntryDto::_id eq productDiaryEntryId.toObjectId(),
                ProductDiaryEntryDto::userId eq userId
            ).wasAcknowledged()
        }
    }

    override suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Unit> {
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

    override suspend fun insertRecipe(recipe: RecipeDto): Resource<Recipe> {
        return handleRequest {
            recipe.apply { recipeCol.insertOne(this) }.toRecipeDiaryEntry()
        }
    }

    override suspend fun getRecipe(recipeId: String): Resource<Recipe?> {
        return handleRequest {
            recipeCol.findOneById(id = recipeId.toObjectId())?.toRecipeDiaryEntry()
        }
    }

    override suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol.find("{'name': {'${MongoOperator.regex}': '$searchText', '${MongoOperator.options}': 'i'}}")
                .toList()
                .map {
                    it.toRecipeDiaryEntry()
                }
        }
    }

    override suspend fun getUserProducts(
        userId: String,
        latestDateTime: LocalDateTime,
    ): Resource<List<Product>> {
        return handleRequest {
            productCol
                .find(ProductDto::userId eq userId)
                .filter(ProductDto::creationDateTime gt latestDateTime)
                .descendingSort(ProductDto::creationDateTime)
                .toList()
                .map { it.toProduct() }
        }
    }

    override suspend fun getUserRecipes(
        userId: String,
        latestDateTime: LocalDateTime,
    ): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol
                .find(RecipeDto::userId eq userId)
                .filter(RecipeDto::creationDateTime gt latestDateTime)
                .descendingSort(RecipeDto::creationDateTime)
                .toList()
                .map { it.toRecipeDiaryEntry() }
        }
    }

    override suspend fun getProductDiaryEntries(userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol
                .find(ProductDiaryEntryDto::userId eq userId)
                .toList()
                .map { it.toProductDiaryEntry() }
        }
    }

    override suspend fun getRecipeDiaryEntries(userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol
                .find(RecipeDiaryEntryDto::userId eq userId)
                .toList()
                .map { it.toRecipeDiaryEntry() }
        }
    }

    override suspend fun getProductDiaryEntries(latestDateTime: LocalDateTime, userId: String): Resource<List<ProductDiaryEntry>> {
        return handleRequest {
            productDiaryCol
                .find(ProductDiaryEntryDto::userId eq userId)
                .filter(ProductDiaryEntryDto::changeDateTime gt latestDateTime)
                .descendingSort(ProductDiaryEntryDto::changeDateTime)
                .toList()
                .map { it.toProductDiaryEntry() }
        }
    }

    override suspend fun getRecipeDiaryEntries(latestDateTime: LocalDateTime, userId: String): Resource<List<RecipeDiaryEntry>> {
        return handleRequest {
            recipeDiaryCol
                .find(RecipeDiaryEntryDto::userId eq userId)
                .filter(RecipeDiaryEntryDto::changeDateTime gt latestDateTime)
                .descendingSort(RecipeDiaryEntryDto::changeDateTime)
                .toList()
                .map { it.toRecipeDiaryEntry() }
        }
    }
}