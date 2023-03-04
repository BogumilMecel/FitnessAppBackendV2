package com.gmail.bogumilmecel2.authentication.domain.model.hash

data class SaltedHash(
    val hash:String,
    val salt:String
)
