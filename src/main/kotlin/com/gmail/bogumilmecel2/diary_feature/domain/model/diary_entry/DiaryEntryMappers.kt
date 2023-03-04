package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId

fun ProductDiaryEntry.toDto(userId: String): ProductDiaryEntryDto = ProductDiaryEntryDto(
    _id = id.toObjectId(),
    timestamp = timestamp,
    userId = userId.toObjectId(),
    nutritionValues = nutritionValues,
    date = date,
    weight = weight,
    mealName = mealName,
    product = product
)

fun ProductDiaryEntryDto.toDiaryEntry(): ProductDiaryEntry = ProductDiaryEntry(
    id = _id.toString(),
    timestamp = timestamp,
    nutritionValues = nutritionValues,
    date = date,
    weight = weight,
    mealName = mealName,
    product = product,
    userId = userId.toString()
)