package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Recipe(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("ingredients")
    val ingredients: List<Ingredient>,

    @SerialName("image_url")
    val imageUrl: String?,

    @SerialName("nutrition_values")
    val nutritionValues: NutritionValues,

    @SerialName("time_required")
    val timeRequired: TimeRequired,

    @SerialName("difficulty")
    val difficulty: Difficulty,

    @SerialName("servings")
    val servings: Int,

    @SerialName("is_public")
    val isPublic: Boolean,

    @SerialName("username")
    val username: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("creation_date")
    val creationDateTime: LocalDateTime
)

data class RecipeDto(
    val _id: ObjectId,
    val name: String,
    val ingredients: List<Ingredient>,
    val imageUrl: String?,
    val nutritionValues: NutritionValues,
    val timeRequired: TimeRequired,
    val difficulty: Difficulty,
    val servings: Int,
    var isPublic: Boolean,
    val userId: String,
    val username: String,
    val creationDateTime: LocalDateTime
)

fun RecipeDto.toRecipe(): Recipe = Recipe(
    id = _id.toString(),
    name = name,
    ingredients = ingredients,
    imageUrl = imageUrl,
    nutritionValues = nutritionValues,
    timeRequired = timeRequired,
    difficulty = difficulty,
    servings = servings,
    username = username,
    userId = userId,
    isPublic = isPublic,
    creationDateTime = creationDateTime
)