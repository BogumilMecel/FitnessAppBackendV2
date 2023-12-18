package com.gmail.bogumilmecel2.common.data.database

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
    ).coroutine.getDatabase(databaseName)
}