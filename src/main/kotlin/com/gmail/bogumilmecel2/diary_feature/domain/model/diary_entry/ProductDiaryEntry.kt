package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.common.domain.model.MeasurementUnit
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.ProductDiaryHistoryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductDiaryEntry(
    @SerialName("id")
    override val id: String = "",

    @SerialName("nutrition_values")
    override val nutritionValues: NutritionValues = NutritionValues(),

    @SerialName("date")
    override val date: LocalDate? = null,

    @SerialName("user_id")
    override val userId: String = "",

    @SerialName("meal_name")
    override val mealName: MealName = MealName.BREAKFAST,

    @SerialName("product_measurement_unit")
    val productMeasurementUnit: MeasurementUnit = MeasurementUnit.GRAMS,

    @SerialName("product_name")
    val productName: String = "",

    @SerialName("product_id")
    val productId: String = "",

    @SerialName("weight")
    val weight: Int = 0,

    // TODO: remove when deleting is handled with device id
    @SerialName("deleted")
    val deleted: Boolean = false,

    @SerialName("creation_date")
    override val creationDateTime: LocalDateTime? = null,

    @SerialName("change_date")
    override val changeDateTime: LocalDateTime? = null
) : DiaryItem

data class ProductDiaryEntryDto(
    @BsonId val _id: ObjectId = ObjectId(),
    val nutritionValues: NutritionValues,
    val date: String,
    val mealName: MealName,
    val userId: String,
    val measurementUnit: MeasurementUnit,
    val productName: String,
    val productId: String,
    val weight: Int,
    val creationDateTime: LocalDateTime? = null,
    val changeDate: LocalDateTime? = null
)

fun ProductDiaryEntry.toDto(userId: String): ProductDiaryEntryDto = ProductDiaryEntryDto(
    _id = id.toObjectId(),
    userId = userId,
    nutritionValues = nutritionValues,
    date = date.toString(),
    weight = weight,
    mealName = mealName,
    productName = productName,
    measurementUnit = productMeasurementUnit,
    productId = productId,
    creationDateTime = creationDateTime,
    changeDate = changeDateTime,
)

fun ProductDiaryEntryDto.toDiaryEntry(): ProductDiaryEntry = ProductDiaryEntry(
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
    changeDateTime = changeDate,
)

fun ProductDiaryEntryDto.toProductDiarySearchItem() = ProductDiaryHistoryItem(
    measurementUnit = measurementUnit,
    nutritionValues = nutritionValues,
    productId = productId,
    productName = productName,
    weight = weight,
    changeDate = changeDate
)