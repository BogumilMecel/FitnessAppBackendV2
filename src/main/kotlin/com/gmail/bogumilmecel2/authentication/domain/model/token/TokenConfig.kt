package com.gmail.bogumilmecel2.authentication.domain.model.token

data class TokenConfig(
    val issuer:String,
    val audience:String,
    val expiresIn:Long,
    val secret:String
)
