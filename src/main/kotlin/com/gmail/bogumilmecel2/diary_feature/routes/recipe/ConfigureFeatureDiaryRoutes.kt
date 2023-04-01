package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.DiaryUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.ProductUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.RecipeUseCases
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configureDeleteDiaryEntryRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configureGetDiaryEntriesRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configureGetUserCaloriesSumRoute
import com.gmail.bogumilmecel2.diary_feature.routes.diary.configurePostDiaryEntryRoute
import com.gmail.bogumilmecel2.diary_feature.routes.product.configureAddNewPriceRoute
import com.gmail.bogumilmecel2.diary_feature.routes.product.configurePostNewProductRoute
import com.gmail.bogumilmecel2.diary_feature.routes.product.configureSearchForProductWithBarcodeRoute
import com.gmail.bogumilmecel2.diary_feature.routes.product.configureSearchForProductWithTextRoute
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
