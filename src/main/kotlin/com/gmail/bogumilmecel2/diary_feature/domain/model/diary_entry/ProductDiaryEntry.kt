package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductDiaryEntry(
    @SerialName("id")
    override val id: String? = null,

    @SerialName("nutrition_values")
    override val nutritionValues: NutritionValues? = null,

    @SerialName("date")
    override val date: LocalDate? = null,

    @SerialName("user_id")
    override val userId: String? = null,

    @SerialName("meal_name")
    override val mealName: MealName? = null,

    @SerialName("product_measurement_unit")
    val productMeasurementUnit: MeasurementUnit? = null,

    @SerialName("product_name")
    val productName: String? = null,

    @SerialName("product_id")
    val productId: String? = null,

    @SerialName("weight")
    val weight: Int? = null,

    // TODO: remove when deleting is handled with device id
    @SerialName("deleted")
    val deleted: Boolean = false,

    @SerialName("creation_date")
    override val creationDateTime: LocalDateTime? = null,

    @SerialName("change_date")
    override val changeDateTime: LocalDateTime? = null
) : DiaryItem

data class ProductDiaryEntryDto(
    @BsonId val _id: ObjectId,
    val nutritionValues: NutritionValues,
    val date: String,
    val mealName: MealName,
    val userId: String,
    val measurementUnit: MeasurementUnit,
    val productName: String,
    val productId: String,
    val weight: Int,
    val creationDateTime: LocalDateTime,
    val changeDateTime: LocalDateTime,
    val deleted: Boolean
)

fun ProductDiaryEntryDto.toProductDiaryEntry(): ProductDiaryEntry = ProductDiaryEntry(
    id = _id.toString(),
    nutritionValues = nutritionValues,
    date = LocalDate.parse(date),
    weight = weight,
    mealName = mealName,
    productId = productId,
    userId = userId,
    productName = productName,
    productMeasurementUnit = measurementUnit,
    creationDateTime = creationDateTime,
    changeDateTime = changeDateTime,
    deleted = deleted
)

fun ProductDiaryEntryDto.toProductDiarySearchItem() = ProductDiaryHistoryItem(
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    productId = productId,
    productName = productName,
    weight = weight,
    changeDate = changeDateTime
)