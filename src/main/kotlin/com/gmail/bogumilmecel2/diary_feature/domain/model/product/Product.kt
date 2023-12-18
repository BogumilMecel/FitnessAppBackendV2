package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Product(
    @SerialName("id")
    val id: String = "",

    @SerialName("name")
    val name: String = "",

    @SerialName("container_weight")
    val containerWeight: Int? = null,

    @SerialName("utc_timestamp")
    val utcTimestamp: Long = 0,

    @SerialName("nutrition_values_in")
    val nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,

    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues = NutritionValues(),

    @SerialName("barcode")
    val barcode: String? = null,

    @SerialName("username")
    val username: String = "",

    @SerialName("user_id")
    val userId: String = ""
)

data class ProductDto(
    @BsonId
    val _id: ObjectId,

    val name: String = "",

    val containerWeight: Int? = null,

    val utcTimestamp: Long = 0,

    val nutritionValuesIn: NutritionValuesIn = NutritionValuesIn.HUNDRED_GRAMS,

    val measurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,

    val nutritionValues: NutritionValues = NutritionValues(),

    val barcode: String? = null,

    val username: String = "",

    val userId: String = "",

    val country: Country = Country.POLAND,

    val date: LocalDateTime? = null
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