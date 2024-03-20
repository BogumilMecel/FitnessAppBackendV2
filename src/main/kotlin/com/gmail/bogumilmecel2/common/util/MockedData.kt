package com.gmail.bogumilmecel2.common.util

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.InsertProductUseCase
import kotlin.random.Random

@Suppress("unused")
object MockedData {
    suspend fun insertSampleProducts(insertProductUseCase: InsertProductUseCase) {
        getSampleProducts().forEach {
            println(
                insertProductUseCase(
                    product = Product(
                        name = it.name!!,
                        measurementUnit = it.measurementUnit!!,
                        containerWeight = it.containerWeight,
                        barcode = null,
                        nutritionValuesIn = it.nutritionValuesIn!!,
                        nutritionValues = it.nutritionValues!!
                    ),
                    country = Country.POLAND,
                    userId = it.userId!!
                )
            )
        }
    }

    private fun getSampleProducts(): List<Product> {
        val productNames = mutableListOf(
            "Apple",
            "Banana",
            "Orange",
            "Milk",
            "Bread",
            "Chicken",
            "Rice",
            "Pasta",
            "Yogurt",
            "Cereal",
            "Salad",
            "Pizza",
            "Fish",
            "Chocolate",
            "Carrot",
            "Broccoli",
            "Egg",
            "Tomato",
            "Potato",
            "Cheese",
            "Beef",
            "Pork",
            "Lettuce",
            "Onion",
            "Cucumber",
            "Water",
            "Juice",
            "Soda",
            "Coffee",
            "Tea",
            "Cookies",
            "Chips",
            "Soup",
            "Sausage",
            "Peanuts",
            "Candy",
            "Nuts",
            "Honey",
            "Butter",
            "Jam",
            "Oatmeal",
            "Cottage Cheese",
            "Biscuits",
            "Pancakes",
            "Waffles",
            "Grapes",
            "Strawberries",
            "Blueberries",
            "Raspberries",
            "Pineapple",
            "Peach",
            "Cherry",
            "Grapefruit",
            "Lemon",
            "Lime"
        )

        return buildList {
            while (productNames.isNotEmpty()) {
                val productNameInt = Random.nextInt(productNames.size)
                val randomProductName = productNames[productNameInt]
                productNames.removeAt(productNameInt)
                val randomCalories = Random.nextInt(200, 400)
                val randomCarbohydrates = Random.nextDouble(10.0, 30.0).round(2)
                val randomProtein = Random.nextDouble(5.0, 20.0).round(2)
                val randomFat = Random.nextDouble(1.0, 15.0).round(2)

                val currentDateTime = CustomDateUtils.getUtcDateTime()

                val product = Product(
                    name = randomProductName,
                    creationDateTime = currentDateTime,
                    nutritionValues = NutritionValues(
                        calories = randomCalories,
                        carbohydrates = randomCarbohydrates,
                        protein = randomProtein,
                        fat = randomFat
                    ),
                    userId = "64996159ea358077adc58469"
                )
                add(product)
            }
        }
    }
}