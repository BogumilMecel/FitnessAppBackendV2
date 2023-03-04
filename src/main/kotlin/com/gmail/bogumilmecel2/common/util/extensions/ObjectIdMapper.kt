package com.gmail.bogumilmecel2.common.util.extensions

import org.bson.types.ObjectId

fun String?.toObjectId(): ObjectId = if (isNullOrEmpty()) ObjectId() else ObjectId(this)