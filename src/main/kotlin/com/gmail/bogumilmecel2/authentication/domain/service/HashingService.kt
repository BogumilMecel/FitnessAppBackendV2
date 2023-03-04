package com.gmail.bogumilmecel2.authentication.domain.service

import com.gmail.bogumilmecel2.authentication.domain.model.hash.SaltedHash

interface HashingService {
    fun generateSaltedHash(value:String, saltLength:Int = 32): SaltedHash
    fun verify(value:String, saltedHash: SaltedHash):Boolean
}