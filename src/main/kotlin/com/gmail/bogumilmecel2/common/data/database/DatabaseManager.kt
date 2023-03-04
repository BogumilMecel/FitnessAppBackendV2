package com.gmail.bogumilmecel2.common.data.database

import com.gmail.bogumilmecel2.authentication.domain.model.user.User
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class DatabaseManager {

    // config
    private val databaseName = "fitness_app"
    private val username = System.getenv("MONGO_USER")
    private val password = System.getenv("MONGO_PW")

    // database
    var client = KMongo.createClient(
        connectionString = "mongodb+srv://FitnessApp:$password@cluster0.e8iazl3.mongodb.net/fitness_app?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(databaseName)

    fun provideUserCollection():CoroutineCollection<User>{
        return client.getCollection()
    }

    fun provideProductCollection(): CoroutineCollection<Product>{
        return client.getCollection()
    }
}