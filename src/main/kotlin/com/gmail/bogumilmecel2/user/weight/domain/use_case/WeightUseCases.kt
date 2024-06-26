package com.gmail.bogumilmecel2.user.weight.domain.use_case

data class WeightUseCases(
    val addWeightEntryUseCase: AddWeightEntryUseCase,
    val checkIfShouldAskForWeightDialogsUseCase: CheckIfShouldAskForWeightDialogsUseCase,
    val handleWeightDialogsAnswerUseCase: HandleWeightDialogsAnswerUseCase
)
