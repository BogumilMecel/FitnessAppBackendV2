package com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.utils.Ingredient
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.RecipePriceResponse
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.getIngredientPriceValue
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.repository.PriceRepository

class GetRecipePriceUseCase(private val priceRepository: PriceRepository) {
    suspend operator fun invoke(
        ingredients: List<Ingredient>,
        currency: Currency,
        country: Country
    ): Resource<RecipePriceResponse> {
        if (ingredients.size > 20) {
            return Resource.Error()
        }
        
        var shouldShowPriceWarning = false
        val totalPrice = ingredients.sumOf { ingredient ->
            val priceResource = priceRepository.getPrice(
                productId = ingredient.productId,
                country = country
            )

            when (priceResource) {
                is Resource.Success -> {
                    priceResource.data?.getIngredientPriceValue(
                        weight = ingredient.weight,
                        currency = currency
                    ) ?: kotlin.run {
                        shouldShowPriceWarning = true
                        0.0
                    }
                }

                is Resource.Error -> {
                    shouldShowPriceWarning = true
                    0.0
                }
            }
        }

        if (totalPrice == 0.0) {
            return Resource.Error()
        }

        return Resource.Success(
            data = RecipePriceResponse(
                totalPrice = totalPrice,
                shouldShowPriceWarning = shouldShowPriceWarning
            )
        )
    }
}