package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import com.gmail.bogumilmecel2.diary.domain.model.product.NutritionValuesIn
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Product(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("container_weight")
    val containerWeight: Int? = null,

    @SerialName("nutrition_values_in")
    val nutritionValuesIn: NutritionValuesIn? = null,

    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnit? = null,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues? = null,

    @SerialName("barcode")
    val barcode: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("user_id")
    val userId: String? = null,

    @SerialName("creation_date")
    val creationDateTime: LocalDateTime? = null
)

data class ProductDto(
    @BsonId
    val _id: ObjectId,

    val name: String,

    val containerWeight: Int?,

    val nutritionValuesIn: NutritionValuesIn,

    val measurementUnit: MeasurementUnit,

    val nutritionValues: NutritionValues,

    val barcode: String?,

    val username: String,

    val userId: String,

    val country: Country,

    val creationDateTime: LocalDateTime
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
    creationDateTime = creationDateTime
)