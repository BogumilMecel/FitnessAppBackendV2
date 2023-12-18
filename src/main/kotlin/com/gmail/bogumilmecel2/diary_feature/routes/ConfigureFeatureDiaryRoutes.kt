package com.gmail.bogumilmecel2.diary_feature.routes

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.DiaryUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.ProductUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.RecipeUseCases
import com.gmail.bogumilmecel2.diary_feature.price_feature.routes.configureAddNewPriceRoute
import com.gmail.bogumilmecel2.diary_feature.price_feature.routes.configureGetRecipePriceRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.*
import com.gmail.bogumilmecel2.diary_feature.routes.product.*
import com.gmail.bogumilmecel2.diary_feature.routes.recipe.*
import io.ktor.server.routing.*


fun Route.configureDiaryRoutes(
    productUseCases: ProductUseCases,
    diaryUseCases: DiaryUseCases,
    recipeUseCases: RecipeUseCases
) {
    route("/products") {
        configurePostNewProductRoute(productUseCases.insertProductUseCase)
        configureSearchForProductWithTextRoute(productUseCases.getProductsUseCase)
        configureSearchForProductWithBarcodeRoute(productUseCases.searchForProductWithBarcode)
        configureAddNewPriceRoute(productUseCases.addNewPriceUseCase)
        configureGetProductPriceRoute(productUseCases.getProductPriceUseCase)
        configureGetProductRoute(productUseCases.getProductUseCase)
    }

    route("/diaryEntries"){
        configurePostDiaryEntryRoute(diaryUseCases.insertProductDiaryEntryUseCase)
        configureGetDiaryEntriesRoute(diaryUseCases.getDiaryEntries)
        configureDeleteDiaryEntryRoute(diaryUseCases.deleteDiaryEntry)
        configureGetUserCaloriesSumRoute(diaryUseCases.getUserCaloriesSum)
        configurePostRecipeDiaryEntry(recipeUseCases.addRecipeDiaryEntryUseCase)
        configureEditProductDiaryEntryRoute(diaryUseCases.editProductDiaryEntryUseCase)
        configureEditRecipeDiaryEntryRoute(diaryUseCases.editRecipeDiaryEntryUseCase)
    }

    route("/recipes"){
        configurePostRecipeRoute(recipeUseCases.insertRecipeUseCase)
        configureSearchForRecipesRoute(recipeUseCases.searchForRecipes)
        configureGetRecipePriceRoute(recipeUseCases.getRecipePriceUseCase)
        configureGetRecipeRoute(recipeUseCases.getRecipeUseCase)
    }
}
