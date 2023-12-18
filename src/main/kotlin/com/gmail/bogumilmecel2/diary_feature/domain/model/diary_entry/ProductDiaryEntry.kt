package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductDiaryEntry(
    override val id: String = "",
    override val nutritionValues: NutritionValues,
    override val timestamp: Long,
    override val date: String,
    override val userId: String,
    override val mealName: MealName,
    val weight: Int,
    val product: Product
) : DiaryItem

data class ProductDiaryEntryDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val nutritionValues: NutritionValues,
    val timestamp: Long,
    val date: String,
    var weight: Int,
    val mealName: MealName,
    val userId: ObjectId,
    val product: Product
)

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
