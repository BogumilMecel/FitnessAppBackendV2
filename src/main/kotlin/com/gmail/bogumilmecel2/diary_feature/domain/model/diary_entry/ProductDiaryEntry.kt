package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductDiaryEntry(
    override val id: String = "",
    override val nutritionValues: NutritionValues = NutritionValues(),
    override val utcTimestamp: Long = 0,
    override val date: String = "",
    override val userId: String = "",
    override val mealName: MealName = MealName.BREAKFAST,
    val productMeasurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,
    val productName: String = "",
    val productId: String = "",
    val weight: Int = 0,
) : DiaryItem

data class ProductDiaryEntryDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val nutritionValues: NutritionValues,
    val utcTimestamp: Long,
    val date: String,
    val mealName: MealName,
    val userId: String,
    val measurementUnit: MeasurementUnit,
    val productName: String,
    val productId: String,
    val weight: Int,
)

fun ProductDiaryEntry.toDto(userId: String): ProductDiaryEntryDto = ProductDiaryEntryDto(
    _id = id.toObjectId(),
    utcTimestamp = utcTimestamp,
    userId = userId,
    nutritionValues = nutritionValues,
    date = date,
    weight = weight,
    mealName = mealName,
    productName = productName,
    measurementUnit = productMeasurementUnit,
    productId = productId
)

fun ProductDiaryEntryDto.toDiaryEntry(): ProductDiaryEntry = ProductDiaryEntry(
    id = _id.toString(),
    utcTimestamp = utcTimestamp,
    nutritionValues = nutritionValues,
    date = date,
    weight = weight,
    mealName = mealName,
    productId = productId,
    userId = userId,
    productName = productName,
    productMeasurementUnit = measurementUnit
)

fun ProductDiaryEntryDto.toProductDiarySearchItem() = ProductDiaryHistoryItem(
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    productId = productId,
    productName = productName,
    weight = weight,
    utcTimestamp = utcTimestamp
)