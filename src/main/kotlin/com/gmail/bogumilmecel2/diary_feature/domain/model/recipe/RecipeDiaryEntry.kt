package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.MealName
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class RecipeDiaryEntry(
    override val id: String = "",
    override val nutritionValues: NutritionValues = NutritionValues(),
    override val utcTimestamp: Long = 0,
    override val userId: String = "",
    override val date: String = "",
    override val mealName: MealName = MealName.BREAKFAST,
    val lastEditedUtcTimestamp: Long,
    val recipeName: String = "",
    val recipeId: String = "",
    val servings: Int = 0,
) : DiaryItem

data class RecipeDiaryEntryDto(
    @BsonId val _id: ObjectId,
    val nutritionValues: NutritionValues,
    val utcTimestamp: Long,
    val lastEditedUtcTimestamp: Long,
    val recipeName: String,
    val recipeId: String,
    val servings: Int,
    val userId: String,
    val date: String,
    val mealName: MealName,
)

fun RecipeDiaryEntry.toDto(
    userId: String,
    currentUtcTimestamp: Long
): RecipeDiaryEntryDto = RecipeDiaryEntryDto(
    _id = id.toObjectId(),
    servings = servings,
    date = date,
    userId = userId,
    utcTimestamp = utcTimestamp,
    nutritionValues = nutritionValues,
    mealName = mealName,
    recipeId = recipeId,
    recipeName = recipeName,
    lastEditedUtcTimestamp = currentUtcTimestamp
)

fun RecipeDiaryEntryDto.toObject(): RecipeDiaryEntry = RecipeDiaryEntry(
    id = _id.toString(),
    servings = servings,
    userId = userId,
    date = date,
    utcTimestamp = utcTimestamp,
    nutritionValues = nutritionValues,
    mealName = mealName,
    recipeName = recipeName,
    recipeId = recipeId,
    lastEditedUtcTimestamp = lastEditedUtcTimestamp
)