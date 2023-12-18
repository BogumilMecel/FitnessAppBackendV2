package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class RecipeDiaryEntry(
    @SerialName("id")
    override val id: String = "",

    @SerialName("nutrition_values")
    override val nutritionValues: NutritionValues = NutritionValues(),

    @SerialName("utc_timestamp")
    override val utcTimestamp: Long = 0,

    @SerialName("date")
    override val date: LocalDate? = null,

    @SerialName("user_id")
    override val userId: String = "",

    @SerialName("meal_name")
    override val mealName: MealName = MealName.BREAKFAST,

    @SerialName("edited_utc_timestamp")
    val editedUtcTimestamp: Long = 0,

    @SerialName("recipe_name")
    val recipeName: String = "",

    @SerialName("recipe_id")
    val recipeId: String = "",

    @SerialName("servings")
    val servings: Int = 0,

    // TODO: remove when deleting is handled with device id
    @SerialName("deleted")
    val deleted: Boolean = false,

    @SerialName("creation_date")
    override val creationDate: LocalDateTime? = null,

    @SerialName("change_date")
    override val changeDate: LocalDateTime? = null
) : DiaryItem

data class RecipeDiaryEntryDto(
    @BsonId val _id: ObjectId,
    val nutritionValues: NutritionValues,
    val utcTimestamp: Long,
    val editedUtcTimestamp: Long,
    val recipeName: String,
    val recipeId: String,
    val servings: Int,
    val userId: String,
    val date: String,
    val mealName: MealName,
    val creationDate: LocalDateTime? = null,
    val changeDate: LocalDateTime? = null
)

fun RecipeDiaryEntry.toDto(userId: String): RecipeDiaryEntryDto = RecipeDiaryEntryDto(
    _id = id.toObjectId(),
    servings = servings,
    date = date.toString(),
    userId = userId,
    utcTimestamp = utcTimestamp,
    nutritionValues = nutritionValues,
    mealName = mealName,
    recipeId = recipeId,
    recipeName = recipeName,
    editedUtcTimestamp = editedUtcTimestamp,
    creationDate = creationDate,
    changeDate = changeDate
)

fun RecipeDiaryEntryDto.toObject(): RecipeDiaryEntry = RecipeDiaryEntry(
    id = _id.toString(),
    servings = servings,
    userId = userId,
    date = LocalDate.parse(date),
    utcTimestamp = utcTimestamp,
    nutritionValues = nutritionValues,
    mealName = mealName,
    recipeName = recipeName,
    recipeId = recipeId,
    editedUtcTimestamp = editedUtcTimestamp,
    creationDate = creationDate,
    changeDate = changeDate
)