package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.domain.model.convertFromDefaultCurrency
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary.domain.model.common.NutritionValues
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class ProductPrice(
    val valueFor100g: Double,
    val valueFor100Calories: Double?,
    val valueFor100Carbohydrates: Double?,
    val valueFor10Protein: Double?,
    val valueFor10Fat: Double?,
)

data class PriceDto(
    @BsonId val _id: ObjectId,
    val productId: String,
    val country: Country,
    val valueFor100gInUSD: Double
)

fun PriceDto.toProductPrice(nutritionValues: NutritionValues, currency: Currency): ProductPrice =
    with(nutritionValues) {
        val valueFor100gInSelectedCurrency = valueFor100gInUSD.convertFromDefaultCurrency(currency = currency)

        return ProductPrice(
            valueFor100g = valueFor100gInSelectedCurrency,
            valueFor100Calories = valueFor100gInSelectedCurrency.calculatePriceValueForNutritionValueFromPriceFor100g(
                forAmount = 100.0,
                nutritionValue = calories.toDouble()
            ),
            valueFor100Carbohydrates = valueFor100gInSelectedCurrency.calculatePriceValueForNutritionValueFromPriceFor100g(
                forAmount = 100.0,
                nutritionValue = carbohydrates
            ),
            valueFor10Protein = valueFor100gInSelectedCurrency.calculatePriceValueForNutritionValueFromPriceFor100g(
                forAmount = 10.0,
                nutritionValue = protein
            ),
            valueFor10Fat = valueFor100gInSelectedCurrency.calculatePriceValueForNutritionValueFromPriceFor100g(
                forAmount = 10.0,
                nutritionValue = fat
            ),
        )
    }

private fun Double.calculatePriceValueForNutritionValueFromPriceFor100g(
    forAmount: Double,
    nutritionValue: Double
) = if (nutritionValue != 0.0) (this / nutritionValue * forAmount).round(2) else null

fun PriceDto.getIngredientPriceValue(weight: Int, currency: Currency): Double {
    val valueForWeightInUsd = valueFor100gInUSD / 100.0 * weight.toDouble()

    return valueForWeightInUsd.convertFromDefaultCurrency(currency = currency)
}