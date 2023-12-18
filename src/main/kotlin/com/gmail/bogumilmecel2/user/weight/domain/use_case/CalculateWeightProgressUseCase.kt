package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry

class CalculateWeightProgressUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        userId: String,
        weightEntries: List<WeightEntry>
    ): Double? {
        if (weightEntries.size < 2) return null

        val mutableWeightEntries = weightEntries.toMutableList().apply { sortByDescending { it.creationDateTime } }

        var (size, midPoint) = with(mutableWeightEntries.size) { this to this / 2 }

        if (size % 2 != 0) {
            mutableWeightEntries.removeAt(midPoint)
            size = mutableWeightEntries.size
            midPoint = size / 2
        }

        val firstAverage = mutableWeightEntries.subList(0, midPoint).map { it.value }.average()
        val secondAverage = mutableWeightEntries.subList(midPoint, size).map { it.value }.average()

        val difference = (firstAverage - secondAverage).round(2)

        userRepository.updateWeightProgress(userId = userId, weightProgress = difference)

        return difference
    }
}