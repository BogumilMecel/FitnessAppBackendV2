package com.gmail.bogumilmecel2.diary_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.CaloriesSumResponse
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.price.Price
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.*
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class DiaryRepositoryImp(
    private val productDiaryCol: CoroutineCollection<ProductDiaryEntryDto>,
    private val recipeDiaryCol: CoroutineCollection<RecipeDiaryEntryDto>,
    private val productCol: CoroutineCollection<ProductDto>,
    private val recipeCol: CoroutineCollection<RecipeDto>
) : DiaryRepository, BaseRepository() {

    override suspend fun insertDiaryEntry(
        productDiaryEntry: ProductDiaryEntry,
        userId: String
    ): Resource<ProductDiaryEntry> {
        return try {
            Resource.Success(
                data = productDiaryEntry.copy(
                    id = productDiaryEntry.toDto(userId = userId).apply {
                        productDiaryCol.insertOne(this)
                    }._id.toString()
                )
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Boolean> {
        return try {
            Resource.Success(
                data = recipeDiaryCol.insertOne(recipeDiaryEntry.toDto(userId = userId)).wasAcknowledged()
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getProductDiaryEntries(date: String, userId: String): Resource<List<ProductDiaryEntry>> {
        return try {
            val productDiaryEntries =
                productDiaryCol.find(
                    ProductDiaryEntryDto::userId eq userId.toObjectId(),
                    ProductDiaryEntryDto::date eq date
                ).toList().map {
                    it.toDiaryEntry()
                }
            Resource.Success(data = productDiaryEntries)
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getRecipeDiaryEntries(date: String, userId: String): Resource<List<RecipeDiaryEntry>> {
        return try {
            Resource.Success(
                data = recipeDiaryCol.find(
                    RecipeDiaryEntryDto::userId eq userId,
                    RecipeDiaryEntryDto::date eq date,
                ).toList().map {
                    it.toObject()
                }
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getDiaryEntry(id: String): Resource<ProductDiaryEntry?> {
        return try {
            Resource.Success(productDiaryCol.findOne(ProductDiaryEntryDto::_id eq id.toObjectId())?.toDiaryEntry())
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getProducts(text: String): Resource<List<Product>> {
        return try {
            Resource.Success(
                data = productCol.find("{'name': {'${MongoOperator.regex}': '$text', '${MongoOperator.options}': 'i'}}")
                    .toList()
                    .map {
                        it.toProduct()
                    }
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getProductHistory(userId: String): Resource<List<Product>> {
        return try {
            val history = mutableListOf<Product>()
            productDiaryCol.find(ProductDiaryEntryDto::userId eq userId.toObjectId()).limit(20).toList().map {
                it.product.id.let { id ->
                    val searchResource = getProduct(productId = id)
                    searchResource.data?.let { product ->
                        history.add(product)
                    }
                }
            }
            Resource.Success(history)
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getProduct(productId: String): Resource<Product?> {
        return try {
            Resource.Success(data = productCol.findOne(ProductDto::_id eq productId.toObjectId())?.toProduct())
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun editDiaryEntry(productDiaryEntry: ProductDiaryEntry): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun insertProduct(product: Product, userId: String): Resource<Product> {
        return try {
            Resource.Success(
                data = product.copy(
                    id = product.toDto(userId).apply { productCol.insertOne(this) }._id.toString()
                )
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun deleteProductDiaryEntry(productDiaryEntryId: String, userId: String): Resource<Unit> {
        return try {
            val wasAcknowledged = productDiaryCol.deleteOne(
                ProductDiaryEntryDto::_id eq productDiaryEntryId.toObjectId(),
                ProductDiaryEntryDto::userId eq userId.toObjectId()
            ).wasAcknowledged()
            if (wasAcknowledged) {
                Resource.Success(data = Unit)
            } else throw Exception()
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun deleteRecipeDiaryEntry(recipeDiaryEntryId: String, userId: String): Resource<Unit> {
        return try {
            val wasAcknowledged = recipeDiaryCol.deleteOne(
                RecipeDiaryEntryDto::_id eq recipeDiaryEntryId.toObjectId(),
                RecipeDiaryEntryDto::userId eq userId
            ).wasAcknowledged()
            if (wasAcknowledged) {
                Resource.Success(data = Unit)
            } else throw Exception()
        } catch (e:Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun searchForProductWithBarcode(barcode: String): Resource<Product?> {
        return try {
            Resource.Success(productCol.findOne(ProductDto::barcode eq barcode)?.toProduct())
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun getUserCaloriesSum(date: String, userId: String): Resource<CaloriesSumResponse> {
        return try {
            Resource.Success(
                CaloriesSumResponse(caloriesSum = productDiaryCol.find(
                    ProductDiaryEntryDto::date eq date,
                    ProductDiaryEntryDto::userId eq userId.toObjectId()
                )
                    .toList()
                    .sumOf { it.nutritionValues.calories })
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun addNewPrice(productId: String, price: Price): Resource<Price> {
        return try {
            if (productCol.updateOneById(
                    id = productId.toObjectId(),
                    update = setValue(ProductDto::price, price)
                ).wasAcknowledged()
            ) {
                Resource.Success(
                    data = price
                )
            } else Resource.Error()
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun addNewRecipe(recipe: Recipe): Resource<Recipe> {
        return try {
            Resource.Success(
                data = recipe.copy(
                    id = recipe.toDto().apply { recipeCol.insertOne(this) }._id.toString()
                )
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    override suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>> {
        return try {
            Resource.Success(
                data = recipeCol.find("{'name': {'${MongoOperator.regex}': '$searchText', '${MongoOperator.options}': 'i'}}")
                    .toList()
                    .map {
                        it.toObject()
                    }
            )
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }
}