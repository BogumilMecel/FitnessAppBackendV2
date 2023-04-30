package com.gmail.bogumilmecel2.diary_feature.data.repository

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.util.BaseRepository
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.CaloriesSumResponse
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.toDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.toProduct
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.*
import com.gmail.bogumilmecel2.diary_feature.domain.repository.DiaryRepository
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

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
        return handleRequest {
            productDiaryEntry.copy(
                id = productDiaryEntry.toDto(userId = userId).apply {
                    productDiaryCol.insertOne(this)
                }._id.toString()
            )
        }
    }

    override suspend fun insertRecipeDiaryEntry(recipeDiaryEntry: RecipeDiaryEntry, userId: String): Resource<Boolean> {
        return handleRequest {
            recipeDiaryCol.insertOne(recipeDiaryEntry.toDto(userId = userId)).wasAcknowledged()
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

    override suspend fun getDiaryEntry(id: String): Resource<ProductDiaryEntry?> {
        return handleRequest {
            productDiaryCol.findOne(ProductDiaryEntryDto::_id eq id.toObjectId())?.toDiaryEntry()
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

    override suspend fun editDiaryEntry(productDiaryEntry: ProductDiaryEntry, userId: String): Resource<Boolean> {
        return handleRequest {
            productDiaryCol.updateOne(
                and(
                    ProductDiaryEntryDto::_id eq productDiaryEntry.id.toObjectId(),
                    ProductDiaryEntryDto::userId eq userId
                ),
                productDiaryEntry.toDto(userId = userId)
            ).wasAcknowledged()
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

    override suspend fun getUserCaloriesSum(date: String, userId: String): Resource<CaloriesSumResponse> {
        return handleRequest {
            CaloriesSumResponse(caloriesSum = productDiaryCol.find(
                ProductDiaryEntryDto::date eq date,
                ProductDiaryEntryDto::userId eq userId
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

    override suspend fun searchForRecipes(searchText: String): Resource<List<Recipe>> {
        return handleRequest {
            recipeCol.find("{'name': {'${MongoOperator.regex}': '$searchText', '${MongoOperator.options}': 'i'}}")
                .toList()
                .map {
                    it.toObject()
                }
        }
    }
}