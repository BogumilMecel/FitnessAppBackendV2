package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Product(
    val id: String = "",
    val name: String = "",
    val containerWeight: Int? = null,
    val utcTimestamp: Long = 0,
    val nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
    val measurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,
    val nutritionValues: NutritionValues = NutritionValues(),
    val barcode: String? = "",
    val username: String = "",
    val userId: String = ""
)

data class ProductDto(
    @BsonId val _id: ObjectId,
    val name: String,
    val containerWeight: Int? = null,
    val utcTimestamp: Long,
    val nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,
    val measurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,
    val nutritionValues: NutritionValues,
    val barcode: String? = null,
    val username: String,
    val userId: String,
    val country: Country = Country.POLAND
)

fun Product.toDto(userId: String, country: Country): ProductDto = ProductDto(
    _id = id.toObjectId(),
    name = name,
    containerWeight = containerWeight,
    nutritionValuesIn = nutritionValuesIn,
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    barcode = barcode,
    userId = userId,
    username = username,
    utcTimestamp = utcTimestamp,
    country = country
)

fun ProductDto.toProduct(): Product = Product(
    id = _id.toString(),
    name = name,
    containerWeight = containerWeight,
    nutritionValuesIn = nutritionValuesIn,
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    barcode = barcode,
    userId = userId,
    username = username,
    utcTimestamp = utcTimestamp
)