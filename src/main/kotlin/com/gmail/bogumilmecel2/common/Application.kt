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
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.*
import com.gmail.bogumilmecel2.diary_feature.price_feature.data.repository.PriceRepositoryImp
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetProductPriceUseCase
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetRecipePriceUseCase
import com.gmail.bogumilmecel2.diary_feature.routes.configureDiaryRoutes
import com.gmail.bogumilmecel2.user.log.domain.use_case.CheckLatestLogEntryAndGetLogStreakUseCase
import com.gmail.bogumilmecel2.user.log.domain.use_case.GetLogEntriesUseCase
import com.gmail.bogumilmecel2.user.log.domain.use_case.InsertLogEntryUseCase
import com.gmail.bogumilmecel2.user.log.domain.use_case.UpdateUserLogStreakUseCase
import com.gmail.bogumilmecel2.user.user_data.data.repository.UserRepositoryImp
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.*
import com.gmail.bogumilmecel2.user.user_data.routes.configureUserDataRoutes
import com.gmail.bogumilmecel2.user.weight.domain.use_case.*
import com.gmail.bogumilmecel2.user.weight.routes.configureWeightRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger = loggerContext.getLogger("org.mongodb.driver")
    rootLogger.level = Level.OFF

    val databaseManager = DatabaseManager()

    GlobalScope.launch {
        databaseManager.listAllIndexes()
    }

    val diaryRepository = DiaryRepositoryImp(
        recipeCol = databaseManager.getRecipeCollection(),
        productCol = databaseManager.getProductCollection(),
        productDiaryCol = databaseManager.getProductDiaryCollection(),
        recipeDiaryCol = databaseManager.getRecipeDiaryCollection()
    )

    val userRepository = UserRepositoryImp(
        userCol = databaseManager.getUserCollection(),
        weightCol = databaseManager.getWeightCollection(),
        logEntryCol = databaseManager.getLogEntryCollection(),
    )

    val getUsernameUseCase = GetUsernameUseCase(userRepository = userRepository)
    val isDiaryNameValidUseCase = IsDiaryNameValidUseCase()
    val getProductUseCase = GetProductUseCase(diaryRepository = diaryRepository)
    val priceRepository = PriceRepositoryImp(
        priceCol = databaseManager.getPriceCollection()
    )

    val calculateNutritionValuesUseCase = CalculateNutritionValuesUseCase()
    val insertProductUseCase = InsertProductUseCase(
        diaryRepository = diaryRepository,
        getUsernameUseCase = getUsernameUseCase,
        isDiaryNameValidUseCase = isDiaryNameValidUseCase,
        areNutritionValuesValid = AreNutritionValuesValid(),
        calculateNutritionValuesUseCase = calculateNutritionValuesUseCase
    )

    val productUseCases = ProductUseCases(
        insertProductUseCase = insertProductUseCase,
        getProductsUseCase = GetProductsUseCase(repository = diaryRepository),
        searchForProductWithBarcode = SearchForProductWithBarcode(diaryRepository),
        addNewPriceUseCase = AddNewPriceUseCase(
            priceRepository = priceRepository,
            getProductUseCase = getProductUseCase,
        ),
        getProductPriceUseCase = GetProductPriceUseCase(
            priceRepository = priceRepository,
            getProductUseCase = getProductUseCase,
        ),
        getProductUseCase = getProductUseCase
    )

    val insertLogEntryUseCase = InsertLogEntryUseCase(userRepository)
    val getLogEntriesUseCase = GetLogEntriesUseCase(userRepository = userRepository)

    val checkLatestLogEntryAndGetLogStreakUseCase = CheckLatestLogEntryAndGetLogStreakUseCase(
        getLogEntriesUseCase = getLogEntriesUseCase,
        insertLogEntryUseCase = insertLogEntryUseCase,
        updateUserLogStreakUseCase = UpdateUserLogStreakUseCase(userRepository)
    )

    val getRecipeDiaryEntryUseCase = GetRecipeDiaryEntryUseCase(diaryRepository = diaryRepository)
    val getProductDiaryEntryUseCase = GetProductDiaryEntryUseCase(diaryRepository = diaryRepository)
    val getRecipeUseCase = GetRecipeUseCase(diaryRepository)
    val getProductDiaryHistoryUseCase = GetProductDiaryHistoryUseCase(diaryRepository = diaryRepository)
    val isTimestampInTwoWeeksUseCase = IsTimestampInTwoWeeksUseCase()

    val diaryUseCases = DiaryUseCases(
        getDiaryEntries = GetDiaryEntries(diaryRepository),
        insertProductDiaryEntryUseCase = InsertProductDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            getProductUseCase = getProductUseCase,
        ),
        deleteDiaryEntry = DeleteDiaryEntry(
            diaryRepository = diaryRepository,
            isTimestampInTwoWeeksUseCase = isTimestampInTwoWeeksUseCase
        ),
        editProductDiaryEntryUseCase = EditProductDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            getProductDiaryEntryUseCase = getProductDiaryEntryUseCase,
            isTimestampInTwoWeeksUseCase = isTimestampInTwoWeeksUseCase
        ),
        editRecipeDiaryEntryUseCase = EditRecipeDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            getRecipeDiaryEntryUseCase = getRecipeDiaryEntryUseCase,
            isTimestampInTwoWeeksUseCase = isTimestampInTwoWeeksUseCase
        ),
        getProductDiaryHistoryUseCase = getProductDiaryHistoryUseCase
    )

    val calculateWeightEntriesUseCase = CalculateWeightProgressUseCase(userRepository)
    val getWeightEntriesUseCase = GetWeightEntriesUseCase(userRepository = userRepository)
    val checkIfWeightIsValidUseCase = CheckIfWeightIsValidUseCase()
    val addWeightEntryUseCase = AddWeightEntryUseCase(
        userRepository = userRepository,
        calculateWeightProgressUseCase = calculateWeightEntriesUseCase,
        getWeightEntriesUseCase = getWeightEntriesUseCase,
        checkIfWeightIsValidUseCase = checkIfWeightIsValidUseCase
    )

    val checkIfShouldAskForWeightDialogsUseCase = CheckIfShouldAskForWeightDialogsUseCase(
        getLogEntriesUseCase = getLogEntriesUseCase,
        getWeightEntriesUseCase = getWeightEntriesUseCase,
        userRepository = userRepository
    )

    val weightUseCases = WeightUseCases(
        addWeightEntryUseCase = addWeightEntryUseCase,
        checkIfShouldAskForWeightDialogsUseCase = checkIfShouldAskForWeightDialogsUseCase,
        handleWeightDialogsAnswerUseCase = HandleWeightDialogsAnswerUseCase(userRepository = userRepository)
    )
    val saveNutritionValuesUseCase = SaveNutritionValuesUseCase(userRepository = userRepository)

    val userDataUseCases = UserDataUseCases(
        saveNutritionValuesUseCase = saveNutritionValuesUseCase,
        handleUserInformationUseCase = HandleUserInformationUseCase(
            userRepository = userRepository,
            saveNutritionValuesUseCase = saveNutritionValuesUseCase,
            calculateNutritionValuesUseCase = CalculateNutritionValuesFromIntroductionUseCase(),
            checkIfWeightIsValidUseCase = checkIfWeightIsValidUseCase,
            addWeightEntryUseCase = addWeightEntryUseCase
        ),
        getUserDiaryItemsUseCase = GetUserDiaryItemsUseCase(diaryRepository)
    )

    val recipeUseCases = RecipeUseCases(
        insertRecipeUseCase = InsertRecipeUseCase(
            diaryRepository = diaryRepository,
            isDiaryNameValidUseCase = isDiaryNameValidUseCase,
            getUsernameUseCase = getUsernameUseCase
        ),
        searchForRecipes = SearchForRecipes(diaryRepository = diaryRepository),
        insertRecipeDiaryEntryUseCase = InsertRecipeDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            getRecipeUseCase = getRecipeUseCase,
        ),
        getRecipePriceUseCase = GetRecipePriceUseCase(priceRepository = priceRepository),
        getRecipeUseCase = getRecipeUseCase
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
                getUserUseCase = GetUserUseCase(
                    getUserObjectUseCase = GetUserObjectUseCase(userRepository = userRepository),
                    checkLatestLogEntryAndGetLogStreakUseCase = checkLatestLogEntryAndGetLogStreakUseCase,
                    getWeightEntriesUseCase = getWeightEntriesUseCase,
                )
            )
        )

        configureWeightRoutes(
            weightUseCases = weightUseCases
        )

        configureUserDataRoutes(
            userDataUseCases = userDataUseCases
        )
    }
}
