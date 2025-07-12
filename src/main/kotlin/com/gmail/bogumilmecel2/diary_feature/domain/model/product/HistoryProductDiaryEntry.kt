package com.gmail.bogumilmecel2.diary_feature.domain.model.product

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class HistoryProductDiaryEntry(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("weight")
    val weight: Int?,
    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnit,
    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues,
    @SerialName("user_id")
    val userId: String,
    @SerialName("product_id")
    val productId: String,
    @SerialName("latest_date_time")
    val latestDateTime: LocalDateTime
)

data class HistoryProductDiaryEntryDto(
    @BsonId
    val _id: ObjectId,

    val name: String,

    val weight: Int?,

    val measurementUnit: MeasurementUnit,

    val nutritionValues: NutritionValues,

    val userId: String,

    val productId: String,

    val latestDateTime: LocalDateTime
)

fun HistoryProductDiaryEntryDto.toHistoryProductDiaryEntry(): HistoryProductDiaryEntry {
    return HistoryProductDiaryEntry(
        id = _id.toHexString(),
        name = name,
        weight = weight,
        measurementUnit = measurementUnit,
        nutritionValues = nutritionValues,
        userId = userId,
        productId = productId,
        latestDateTime = latestDateTime
    )
}
