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
    override val id: String,
    override val nutritionValues: NutritionValues,
    override val timestamp: Long,
    override val userId: String,
    override val date: String,
    override val mealName: MealName,
    val recipe: Recipe,
    val servings: Int,
) : DiaryItem

data class RecipeDiaryEntryDto(
    @BsonId val _id: ObjectId,
    val nutritionValues: NutritionValues,
    val timestamp: Long,
    val recipe: Recipe,
    val servings: Int,
    val userId: String,
    val date: String,
    val mealName: MealName,
)

fun RecipeDiaryEntry.toDto(userId: String): RecipeDiaryEntryDto = RecipeDiaryEntryDto(
    _id = id.toObjectId(),
    recipe = recipe,
    servings = servings,
    date = date,
    userId = userId,
    timestamp = timestamp,
    nutritionValues = nutritionValues,
    mealName = mealName
)

fun RecipeDiaryEntryDto.toObject(): RecipeDiaryEntry = RecipeDiaryEntry(
    id = _id.toString(),
    recipe = recipe,
    servings = servings,
    userId = userId,
    date = date,
    timestamp = timestamp,
    nutritionValues = nutritionValues,
    mealName = mealName
)
