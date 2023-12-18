package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Recipe(
    @SerialName("id")
    val id: String = "",

    @SerialName("name")
    val name: String = "",

    @SerialName("ingredients")
    val ingredients: List<Ingredient> = emptyList(),

    @SerialName("utc_timestamp")
    val utcTimestamp: Long = System.currentTimeMillis(),

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues = NutritionValues(),

    @SerialName("time_required")
    val timeRequired: TimeRequired = TimeRequired.LOW,

    @SerialName("difficulty")
    val difficulty: Difficulty = Difficulty.LOW,

    @SerialName("servings")
    val servings: Int = 0,

    @SerialName("is_public")
    val isPublic: Boolean = false,

    @SerialName("username")
    val username: String = "",

    @SerialName("user_id")
    val userId: String = "",
)

data class RecipeDto(
    val _id: ObjectId,
    val name: String,
    val ingredients: List<Ingredient>,
    val utcTimestamp: Long,
    val imageUrl: String?,
    val nutritionValues: NutritionValues,
    val timeNeeded: TimeRequired,
    val difficulty: Difficulty,
    val servings: Int,
    var isPublic: Boolean,
    val userId: String,
    val username: String
)

fun Recipe.toDto(): RecipeDto = RecipeDto(
    _id = id.toObjectId(),
    name = name,
    ingredients = ingredients,
    utcTimestamp = utcTimestamp,
    imageUrl = imageUrl,
    timeNeeded = timeRequired,
    difficulty = difficulty,
    servings = servings,
    userId = userId,
    username = username,
    isPublic = isPublic,
    nutritionValues = nutritionValues
)

fun RecipeDto.toObject(): Recipe = Recipe(
    id = _id.toString(),
    name = name,
    ingredients = ingredients,
    utcTimestamp = utcTimestamp,
    imageUrl = imageUrl,
    nutritionValues = nutritionValues,
    timeRequired = timeNeeded,
    difficulty = difficulty,
    servings = servings,
    username = username,
    userId = userId,
    isPublic = isPublic,
)