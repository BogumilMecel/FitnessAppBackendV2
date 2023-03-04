package com.gmail.bogumilmecel2.diary_feature.domain.model.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Difficulty
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.TimeRequired
import kotlinx.serialization.Serializable

@Serializable
data class NewRecipeRequest(
    val recipeName: String,
    val servings: Int,
    val timeRequired: TimeRequired,
    val difficulty: Difficulty,
    val ingredients: List<Ingredient>,
    val timestamp: Long,
    val isPublic: Boolean
)