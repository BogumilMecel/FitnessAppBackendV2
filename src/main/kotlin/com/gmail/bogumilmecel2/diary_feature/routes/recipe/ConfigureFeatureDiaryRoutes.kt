package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.DiaryUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.ProductUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.RecipeUseCases
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configureDeleteDiaryEntryRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configureGetDiaryEntriesRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configureGetUserCaloriesSumRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configurePostDiaryEntryRoute
import com.gmail.bogumilmecel2.diary_feature.routes.product.*
import io.ktor.server.routing.*


fun Route.configureDiaryRoutes(
    productUseCases: ProductUseCases,
    diaryUseCases: DiaryUseCases,
    recipeUseCases: RecipeUseCases
) {
    route("/products") {
        configurePostNewProductRoute(productUseCases.insertProductUseCase)
        configureSearchForProductWithTextRoute(productUseCases.getProducts)
        configureGetProductHistoryRoute(productUseCases.getProductHistoryUseCase)
        configureSearchForProductWithBarcodeRoute(productUseCases.searchForProductWithBarcode)
        configureAddNewPriceRoute(productUseCases.addNewPrice)
    }

    route("/diaryEntries"){
        configurePostDiaryEntryRoute(diaryUseCases.insertProductDiaryEntryUseCase)
        configureGetDiaryEntriesRoute(diaryUseCases.getDiaryEntries)
        configureDeleteDiaryEntryRoute(diaryUseCases.deleteDiaryEntry)
        configureGetUserCaloriesSumRoute(diaryUseCases.getUserCaloriesSum)
        configurePostRecipeDiaryEntry(recipeUseCases.addRecipeDiaryEntryUseCase)
    }

    route("/recipes"){
        configurePostRecipeRoute(recipeUseCases.insertRecipeUseCase)
        configureSearchForRecipesRoute(recipeUseCases.searchForRecipes)
    }
}
