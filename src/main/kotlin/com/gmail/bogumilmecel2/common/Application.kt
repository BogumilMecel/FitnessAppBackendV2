package com.gmail.bogumilmecel2.common

import com.gmail.bogumilmecel2.authentication.data.service.JwtTokenService
import com.gmail.bogumilmecel2.authentication.data.service.SHA256HashingService
import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.use_case.AuthUseCases
import com.gmail.bogumilmecel2.authentication.domain.use_case.CheckIfUsernameExists
import com.gmail.bogumilmecel2.authentication.domain.use_case.GetUserByUsernameUseCase
import com.gmail.bogumilmecel2.authentication.domain.use_case.RegisterNewUserUseCase
import com.gmail.bogumilmecel2.authentication.routes.configureAuthRoutes
import com.gmail.bogumilmecel2.common.data.database.DatabaseManager
import com.gmail.bogumilmecel2.common.domain.use_case.GetUsernameUseCase
import com.gmail.bogumilmecel2.common.plugins.*
import com.gmail.bogumilmecel2.diary_feature.data.repository.DiaryRepositoryImp
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.CalculateSkipUseCase
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.*
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.*
import com.gmail.bogumilmecel2.diary_feature.price_feature.data.repository.PriceRepositoryImp
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetProductPriceUseCase
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetRecipePriceUseCase
import com.gmail.bogumilmecel2.diary_feature.routes.configureDiaryRoutes
import com.gmail.bogumilmecel2.user.dates.domain.use_cases.GetAvailableDiaryDatesUseCase
import com.gmail.bogumilmecel2.user.dates.routes.configureGetAvailableDiaryDatesRoute
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

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    val databaseManager = DatabaseManager()

    val diaryRepository = DiaryRepositoryImp(
        recipeCol = databaseManager.getRecipeCollection(),
        productCol = databaseManager.getProductCollection(),
        productDiaryCol = databaseManager.getProductDiaryCollection(),
        recipeDiaryCol = databaseManager.getRecipeDiaryCollection(),
        historyProductDiaryEntryCol = databaseManager.getHistoryProductDiaryEntryCollection()
    )

    val userRepository = UserRepositoryImp(
        userCol = databaseManager.getUserCollection(),
        weightCol = databaseManager.getWeightCollection(),
        logEntryCol = databaseManager.getLogEntryCollection(),
        weightDialogsQuestionCol = databaseManager.getWeightDialogsQuestionCol()
    )

//    val deviceRepository = DeviceRepositoryImp(deviceCollection = databaseManager.getDeviceCollection())

    val getUsernameUseCase = GetUsernameUseCase(userRepository = userRepository)
    val isDiaryNameValidUseCase = IsDiaryNameValidUseCase()
    val getProductDtoUseCase = GetProductDtoUseCase(diaryRepository = diaryRepository)
    val priceRepository = PriceRepositoryImp(
        priceCol = databaseManager.getPriceCollection()
    )

    val calculateNutritionValuesUseCase = CalculateNutritionValuesUseCase()
    val insertProductUseCase = InsertProductUseCase(
        diaryRepository = diaryRepository,
        getUsernameUseCase = getUsernameUseCase,
        isDiaryNameValidUseCase = isDiaryNameValidUseCase,
        areNutritionValuesValidUseCase = AreNutritionValuesValidUseCase(),
        calculateNutritionValuesUseCase = calculateNutritionValuesUseCase
    )

    val postHistoryProductDiaryEntryUseCase = PostHistoryProductDiaryEntryUseCase(diaryRepository = diaryRepository)

    val productUseCases = ProductUseCases(
        insertProductUseCase = insertProductUseCase,
        getProductsUseCase = GetProductsUseCase(
            repository = diaryRepository,
            calculateSkipUseCase = CalculateSkipUseCase()
        ),
        addNewPriceUseCase = AddNewPriceUseCase(
            priceRepository = priceRepository,
            getProductDtoUseCase = getProductDtoUseCase,
        ),
        getProductPriceUseCase = GetProductPriceUseCase(
            priceRepository = priceRepository,
            getProductDtoUseCase = getProductDtoUseCase,
        ),
        getProductDtoUseCase = getProductDtoUseCase,
        postHistoryProductDiaryEntryUseCase = postHistoryProductDiaryEntryUseCase,
        getProductUseCase = GetProductUseCase(diaryRepository)
    )

    val insertLogEntryUseCase = InsertLogEntryUseCase(userRepository)
    val getLogEntriesUseCase = GetLogEntriesUseCase(userRepository = userRepository)

    val checkLatestLogEntryAndGetLogStreakUseCase = CheckLatestLogEntryAndGetLogStreakUseCase(
        getLogEntriesUseCase = getLogEntriesUseCase,
        insertLogEntryUseCase = insertLogEntryUseCase,
        updateUserLogStreakUseCase = UpdateUserLogStreakUseCase(userRepository)
    )

    val getProductDiaryEntryUseCase = GetProductDiaryEntryUseCase(diaryRepository = diaryRepository)
    val getRecipeUseCase = GetRecipeUseCase(diaryRepository)
    val getProductDiaryHistoryUseCase = GetProductDiaryHistoryUseCase(diaryRepository = diaryRepository)
    val getHistoryProductDiaryEntriesUseCase = GetHistoryProductDiaryEntriesUseCase(diaryRepository = diaryRepository)
    val isDateInValidRangeUseCaseUseCase = IsDateInValidRangeUseCaseUseCase()

    val diaryUseCases = DiaryUseCases(
        getDiaryEntriesUseCase = GetDiaryEntriesUseCase(diaryRepository),
        insertProductDiaryEntryUseCase = InsertProductDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase,
            postHistoryProductDiaryEntryUseCase = postHistoryProductDiaryEntryUseCase,
        ),
        deleteDiaryEntryUseCase = DeleteDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
        ),
        editProductDiaryEntryUseCase = EditProductDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            getProductDiaryEntryUseCase = getProductDiaryEntryUseCase,
            isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
        ),
        editRecipeDiaryEntryUseCase = EditRecipeDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
        ),
        getProductDiaryHistoryUseCase = getProductDiaryHistoryUseCase,
        getHistoryProductDiaryEntriesUseCase = getHistoryProductDiaryEntriesUseCase
    )

    val calculateWeightEntriesUseCase = CalculateWeightProgressUseCase(userRepository)
    val getWeightEntriesUseCase = GetWeightEntriesUseCase(userRepository = userRepository)
    val checkIfWeightIsValidUseCase = CheckIfWeightIsValidUseCase()
    val addWeightEntryUseCase = AddWeightEntryUseCase(
        userRepository = userRepository,
        calculateWeightProgressUseCase = calculateWeightEntriesUseCase,
        checkIfWeightIsValidUseCase = checkIfWeightIsValidUseCase,
        isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase,
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
        getUserProductDiaryEntriesUseCase = GetUserProductDiaryEntriesUseCase(diaryRepository),
        getUserRecipeDiaryEntriesUseCase = GetUserRecipeDiaryEntriesUseCase(diaryRepository),
        getUserProductsUseCase = GetUserProductsUseCase(diaryRepository),
        getUserRecipesUseCase = GetUserRecipesUseCase(diaryRepository),
        getDiaryCacheUseCase = GetDiaryCacheUseCase(diaryRepository, userRepository)
    )

    val getAvailableDiaryDatesUseCase = GetAvailableDiaryDatesUseCase(userRepository = userRepository)

    val recipeUseCases = RecipeUseCases(
        insertRecipeUseCase = InsertRecipeUseCase(
            diaryRepository = diaryRepository,
            isDiaryNameValidUseCase = isDiaryNameValidUseCase,
            getUsernameUseCase = getUsernameUseCase
        ),
        searchForRecipes = SearchForRecipes(diaryRepository = diaryRepository),
        insertRecipeDiaryEntryUseCase = InsertRecipeDiaryEntryUseCase(
            diaryRepository = diaryRepository,
            isDateInValidRangeUseCaseUseCase = isDateInValidRangeUseCaseUseCase
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

    val getUserByUsernameUseCase = GetUserByUsernameUseCase(
        userRepository = userRepository,
        hashingService = hashingService,
        tokenService = tokenService,
    )

    configureAuthentication(tokenConfig = tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureLocations()
    configureInternationalization()

    GlobalScope.launch {
        databaseManager.listAllIndexes()
//        MockedData.insertSampleProducts(insertProductUseCase = insertProductUseCase)
//        databaseManager.createProductNameIndex()
    }

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
                    checkIfUsernameExists = CheckIfUsernameExists(userRepository = userRepository),
                    getUserByUsernameUseCase = getUserByUsernameUseCase
                ),
                getUserByUsernameUseCase = getUserByUsernameUseCase,
                getUserUseCase = GetUserUseCase(
                    checkLatestLogEntryAndGetLogStreakUseCase = checkLatestLogEntryAndGetLogStreakUseCase,
                    getWeightEntriesUseCase = getWeightEntriesUseCase,
//                    deviceRepository = deviceRepository,
                    userRepository = userRepository
                )
            )
        )

        configureWeightRoutes(weightUseCases = weightUseCases)

        configureUserDataRoutes(userDataUseCases = userDataUseCases)
        configureGetAvailableDiaryDatesRoute(getAvailableDiaryDatesUseCase = getAvailableDiaryDatesUseCase)
    }
}
