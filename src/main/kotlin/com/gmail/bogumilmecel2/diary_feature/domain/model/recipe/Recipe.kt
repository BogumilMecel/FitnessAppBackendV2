package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.common.util.extensions.toObjectId
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.price.Price
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Recipe(
    val id: String = "",
    val name: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val nutritionValues: NutritionValues = NutritionValues(),
    val timeRequired: TimeRequired = TimeRequired.LOW,
    val difficulty: Difficulty = Difficulty.LOW,
    val servings: Int = 0,
    var isPublic: Boolean = false,
    val username: String = "",
    val userId: String = "",
    val price: Price? = null
)

data class RecipeDto(
    val _id: ObjectId,
    val name: String,
    val ingredients: List<Ingredient>,
    val timestamp: Long,
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
    timestamp = timestamp,
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
    timestamp = timestamp,
    imageUrl = imageUrl,
    nutritionValues = nutritionValues,
    timeRequired = timeNeeded,
    difficulty = difficulty,
    servings = servings,
    username = username,
    userId = userId,
    isPublic = isPublic,
)

fun Recipe.calculateNutritionValues(servings: Int): NutritionValues {
    val nutritionValues = this.nutritionValues
    return NutritionValues(
        calories = (nutritionValues.calories.toDouble() * (servings.toDouble() / this.servings.toDouble())).toInt(),
        carbohydrates = (nutritionValues.carbohydrates * (servings.toDouble() / this.servings.toDouble())).round(2),
        protein = (nutritionValues.protein * (servings.toDouble() / this.servings.toDouble())).round(2),
        fat = (nutritionValues.fat * (servings.toDouble() / this.servings.toDouble())).round(2)
    )
}
