package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

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
    override val id: String? = null,

    @SerialName("nutrition_values")
    override val nutritionValues: NutritionValues? = null,

    @SerialName("date")
    override val date: LocalDate? = null,

    @SerialName("user_id")
    override val userId: String? = null,

    @SerialName("meal_name")
    override val mealName: MealName? = null,

    @SerialName("recipe_name")
    val recipeName: String? = null,

    @SerialName("recipe_id")
    val recipeId: String? = null,

    @SerialName("servings")
    val servings: Int? = null,

    // TODO: remove when deleting is handled with device id
    @SerialName("deleted")
    val deleted: Boolean = false,

    @SerialName("creation_date")
    override val creationDateTime: LocalDateTime? = null,

    @SerialName("change_date")
    override val changeDateTime: LocalDateTime? = null
) : DiaryItem

data class RecipeDiaryEntryDto(
    @BsonId val _id: ObjectId,
    val nutritionValues: NutritionValues,
    val recipeName: String,
    val recipeId: String,
    val servings: Int,
    val userId: String,
    val date: String,
    val mealName: MealName,
    val creationDateTime: LocalDateTime,
    val changeDateTime: LocalDateTime,
    val deleted: Boolean
)

fun RecipeDiaryEntryDto.toRecipeDiaryEntry(): RecipeDiaryEntry = RecipeDiaryEntry(
    id = _id.toString(),
    servings = servings,
    userId = userId,
    date = LocalDate.parse(date),
    nutritionValues = nutritionValues,
    mealName = mealName,
    recipeName = recipeName,
    recipeId = recipeId,
    creationDateTime = creationDateTime,
    changeDateTime = changeDateTime,
    deleted = deleted
)