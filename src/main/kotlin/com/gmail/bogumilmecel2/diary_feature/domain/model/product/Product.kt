package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.price.Price
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Product(
    val id: String = "",
    val name: String,
    val containerWeight: Int? = null,
    val timestamp: Long,
    val nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
    val measurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,
    val nutritionValues: NutritionValues,
    val barcode: String? = null,
    val price: Price? = null,
    val username: String,
    val userId: String
)

data class ProductDto(
    @BsonId val _id: ObjectId,
    val name: String,
    val containerWeight: Int? = null,
    val timestamp: Long,
    val nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
    val measurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,
    val nutritionValues: NutritionValues,
    val barcode: String? = null,
    val price: Price? = null,
    val username: String,
    val userId: String
)

fun Product.toDto(userId: String): ProductDto = ProductDto(
    _id = id.toObjectId(),
    name = name,
    containerWeight = containerWeight,
    nutritionValuesIn = nutritionValuesIn,
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    barcode = barcode,
    price = price,
    userId = userId,
    username = username,
    timestamp = timestamp
)

fun ProductDto.toProduct(): Product = Product(
    id = _id.toString(),
    name = name,
    containerWeight = containerWeight,
    nutritionValuesIn = nutritionValuesIn,
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    barcode = barcode,
    price = price,
    userId = userId,
    username = username,
    timestamp = timestamp
)

fun Product.calculateNutritionValues(weight: Int): NutritionValues {
    return NutritionValues(
        calories = ((nutritionValues.calories).toDouble() / 100.0 * weight.toDouble()).toInt(),
        carbohydrates = nutritionValues.carbohydrates / 100.0 * weight.toDouble(),
        protein = nutritionValues.protein / 100.0 * weight.toDouble(),
        fat = nutritionValues.fat / 100.0 * weight.toDouble(),
    )
}


