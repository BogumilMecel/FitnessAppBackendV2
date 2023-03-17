package com.gmail.bogumilmecel2.common

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.gmail.bogumilmecel2.authentication.data.service.JwtTokenService
import com.gmail.bogumilmecel2.authentication.data.service.SHA256HashingService
import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.use_case.AuthUseCases
import com.gmail.bogumilmecel2.authentication.domain.use_case.CheckIfUsernameExists
import com.gmail.bogumilmecel2.authentication.domain.use_case.GetUserByUsername
import com.gmail.bogumilmecel2.authentication.domain.use_case.RegisterNewUserUseCase
import com.gmail.bogumilmecel2.authentication.routes.configureAuthRoutes
import com.gmail.bogumilmecel2.common.data.database.DatabaseManager
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.plugins.configureAuthentication
import com.gmail.bogumilmecel2.common.plugins.configureLocations
import com.gmail.bogumilmecel2.common.plugins.configureMonitoring
import com.gmail.bogumilmecel2.common.plugins.configureSerialization
import com.gmail.bogumilmecel2.diary_feature.data.repository.DiaryRepositoryImp
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.IsDiaryNameValidUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.AddRecipeDiaryEntryUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.InsertRecipeUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.RecipeUseCases
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.SearchForRecipes
import com.gmail.bogumilmecel2.diary_feature.price_feature.data.repository.PriceRepositoryImp
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetPriceUseCase
import com.gmail.bogumilmecel2.diary_feature.routes.recipe.configureDiaryRoutes
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntry
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLatestLogEntry
import com.gmail.bogumilmecel2.user.log.domain.use_case.InsertLogEntry
import com.gmail.bogumilmecel2.user.log.routes.configureLogRoutes
import com.gmail.bogumilmecel2.user.user_data.data.repository.UserRepositoryImp
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.*
import com.gmail.bogumilmecel2.user.user_data.routes.configureUserDataRoutes
import com.gmail.bogumilmecel2.user.weight.domain.use_case.AddWeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.use_case.CalculateWeightProgressUseCase
import com.gmail.bogumilmecel2.user.weight.domain.use_case.GetWeightEntriesUseCase
import com.gmail.bogumilmecel2.user.weight.domain.use_case.WeightUseCases
import com.gmail.bogumilmecel2.user.weight.routes.configureWeightRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = loggerContext.getLogger("org.mongodb.driver")
    rootLogger.level = Level.OFF

    val databaseManager = DatabaseManager()

    val diaryRepository = DiaryRepositoryImp(
        recipeCol = databaseManager.client.getCollection("recipe_collection"),
        productCol = databaseManager.client.getCollection("product_collection"),
        productDiaryCol = databaseManager.client.getCollection("diary_collection"),
        recipeDiaryCol = databaseManager.client.getCollection("recipe_diary_collection")
    )

    val userRepository = UserRepositoryImp(
        userCol = databaseManager.client.getCollection("user_collection"),
        weightCol = databaseManager.client.getCollection("weight_collection")
    )

    val getUsernameUseCase = GetUsernameUseCase(userRepository = userRepository)
    val isDiaryNameValidUseCase = IsDiaryNameValidUseCase()
    val getProductUseCase = GetProductUseCase(diaryRepository = diaryRepository)
    val priceRepository = PriceRepositoryImp(
        priceCol = databaseManager.client.getCollection("price_collection")
    )
    val getPriceUseCase = GetPriceUseCase(priceRepository)

    val productUseCases = ProductUseCases(
        insertProductUseCase = InsertProductUseCase(
            diaryRepository = diaryRepository,
            getUsernameUseCase = getUsernameUseCase,
            isDiaryNameValidUseCase = isDiaryNameValidUseCase
        ),
        getProductsUseCase = GetProductsUseCase(repository = diaryRepository, getPrice = getPriceUseCase),
        getProductHistoryUseCase = GetProductHistoryUseCase(diaryRepository),
        searchForProductWithBarcode = SearchForProductWithBarcode(diaryRepository),
        addNewPriceUseCase = AddNewPriceUseCase(
            priceRepository = priceRepository,
            getProductUseCase = getProductUseCase
        )
    )

    val getLatestLogEntry = GetLatestLogEntry(userRepository)
    val insertLogEntry = InsertLogEntry(userRepository)

    val checkLatestLogEntry = CheckLatestLogEntry(
        getLatestLogEntry = getLatestLogEntry,
        insertLogEntry = insertLogEntry
    )

    val diaryUseCases = DiaryUseCases(
        getDiaryEntries = GetDiaryEntries(diaryRepository),
        insertProductDiaryEntryUseCase = InsertProductDiaryEntryUseCase(diaryRepository),
        deleteDiaryEntry = DeleteDiaryEntry(diaryRepository),
        getUserCaloriesSum = GetUserCaloriesSum(diaryRepository)
    )

    val calculateWeightEntriesUseCase = CalculateWeightProgressUseCase()
    val getWeightEntriesUseCase = GetWeightEntriesUseCase(userRepository = userRepository)

    val weightUseCases = WeightUseCases(
        addWeightEntry = AddWeightEntry(
            userRepository = userRepository,
            calculateWeightProgressUseCase = calculateWeightEntriesUseCase,
            getWeightEntriesUseCase = getWeightEntriesUseCase
        )
    )
    val saveNutritionValuesUseCase = SaveNutritionValuesUseCase(userRepository = userRepository)

    val userDataUseCases = UserDataUseCases(
        saveNutritionValuesUseCase = saveNutritionValuesUseCase,
        handleUserInformationUseCase = HandleUserInformationUseCase(
            userRepository = userRepository,
            saveNutritionValuesUseCase = saveNutritionValuesUseCase,
            calculateNutritionValuesUseCase = CalculateNutritionValuesUseCase()
        )
    )

    val recipeUseCases = RecipeUseCases(
        insertRecipeUseCase = InsertRecipeUseCase(
            diaryRepository = diaryRepository,
            isDiaryNameValidUseCase = isDiaryNameValidUseCase,
            getUsernameUseCase = getUsernameUseCase
        ),
        searchForRecipes = SearchForRecipes(diaryRepository = diaryRepository),
        addRecipeDiaryEntryUseCase = AddRecipeDiaryEntryUseCase(diaryRepository = diaryRepository)
    )

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("ktor.jwt.issuer").getString(),
        audience = environment.config.property("ktor.jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    val hashingService = SHA256HashingService()

    configureAuthentication(
        tokenConfig = tokenConfig
    )
    configureMonitoring()
    configureSerialization()
    configureLocations()



    routing {
        configureDiaryRoutes(
            productUseCases = productUseCases,
            diaryUseCases = diaryUseCases,
            recipeUseCases = recipeUseCases
        )

        configureAuthRoutes(
            tokenConfig = tokenConfig,
            authUseCases = AuthUseCases(
                registerNewUserUseCase = RegisterNewUserUseCase(
                    userRepository = userRepository,
                    hashingService = hashingService,
                    checkIfUsernameExists = CheckIfUsernameExists(userRepository = userRepository)
                ),
                getUserByUsername = GetUserByUsername(
                    userRepository = userRepository,
                    hashingService = hashingService,
                    tokenService = tokenService,
                ),
                getUser = GetUser(
                    userRepository = userRepository,
                    checkLatestLogEntry = checkLatestLogEntry,
                    getWeightEntriesUseCase = getWeightEntriesUseCase,
                    calculateWeightProgressUseCase = calculateWeightEntriesUseCase
                )
            )
        )

        configureLogRoutes(
            checkLatestLogEntry = checkLatestLogEntry
        )

        configureWeightRoutes(
            weightUseCases = weightUseCases
        )

        configureUserDataRoutes(
            userDataUseCases = userDataUseCases
        )
    }
}
