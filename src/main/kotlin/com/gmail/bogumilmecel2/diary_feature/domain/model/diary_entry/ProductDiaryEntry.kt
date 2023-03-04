package com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry

import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryItem
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductDiaryEntry(
    override val id: String = "",
    override val nutritionValues: NutritionValues,
    override val timestamp: Long,
    override val date: String,
    override val userId: String,
    override val mealName: String,
    var weight: Int,
    val product: Product
) : DiaryItem
