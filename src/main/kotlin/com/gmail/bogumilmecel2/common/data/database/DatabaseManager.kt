package com.gmail.bogumilmecel2.common.data.database

import com.mongodb.kotlin.client.coroutine.MongoClient

class DatabaseManager {

    // config
    private val databaseName = "fitness_app"
    private val username = System.getenv("MONGO_USER")
    private val password = System.getenv("MONGO_PW")

    // database
    var client = MongoClient.create(
        connectionString = "mongodb+srv://FitnessApp:$password@cluster0.e8iazl3.mongodb.net/fitness_app?retryWrites=true&w=majority"
    ).getDatabase(databaseName)
}