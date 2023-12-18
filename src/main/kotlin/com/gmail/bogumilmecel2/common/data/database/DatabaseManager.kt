package com.gmail.bogumilmecel2.common.data.database

import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.ProductDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryDto
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDto
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.PriceDto
import com.gmail.bogumilmecel2.user.log.domain.model.LogEntryDto
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntryDto
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class DatabaseManager {

    // config
    private val databaseName = "fitness_app"
    private val password = System.getenv("MONGO_PW")

    // database
    private val client = KMongo.createClient(
        connectionString = "mongodb+srv://FitnessApp:$password@cluster0.e8iazl3.mongodb.net/fitness_app?retryWrites=true&w=majority"
    ).coroutine.getDatabase(databaseName)

    fun getProductDiaryCollection(): CoroutineCollection<ProductDiaryEntryDto> = client.getCollection("diary_collection")
    fun getRecipeDiaryCollection(): CoroutineCollection<RecipeDiaryEntryDto> = client.getCollection("recipe_diary_collection")
    fun getRecipeCollection(): CoroutineCollection<RecipeDto> = client.getCollection("recipe_collection")
    fun getProductCollection(): CoroutineCollection<ProductDto> = client.getCollection("product_collection")
    fun getUserCollection(): CoroutineCollection<UserDto> = client.getCollection("user_collection")
    fun getWeightCollection(): CoroutineCollection<WeightEntryDto> = client.getCollection("weight_collection")
    fun getLogEntryCollection(): CoroutineCollection<LogEntryDto> = client.getCollection("log_entry_collection")
    fun getPriceCollection(): CoroutineCollection<PriceDto> = client.getCollection("price_collection")

    suspend fun listAllIndexes() {
        listIndexes(getProductDiaryCollection())
        listIndexes(getRecipeDiaryCollection())
        listIndexes(getRecipeCollection())
        listIndexes(getProductCollection())
        listIndexes(getUserCollection())
        listIndexes(getWeightCollection())
        listIndexes(getLogEntryCollection())
        listIndexes(getPriceCollection())
    }

    private suspend inline fun <reified T: Any> listIndexes(collection: CoroutineCollection<T>) {
        println("${T::class.java.simpleName} indexes: ${collection.listIndexes<Any>().toList()}")
    }

    suspend fun <T: Any> removeIndex(
        collection: CoroutineCollection<T>,
        indexName: String
    ) = collection.dropIndex(indexName = indexName)

    suspend fun <T: Any> createIndex(
        collection: CoroutineCollection<T>,
        key: String
    ) = collection.createIndex(key = key)
}