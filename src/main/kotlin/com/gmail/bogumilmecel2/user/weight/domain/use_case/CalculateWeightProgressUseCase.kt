package com.gmail.bogumilmecel2.user.weight.domain.use_case

import com.gmail.bogumilmecel2.common.util.extensions.round
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry

class CalculateWeightProgressUseCase {
    operator fun invoke(weightEntries:List<WeightEntry>):String?{
        if (weightEntries.size > 1) {
            weightEntries.sortedByDescending { it.timestamp }.also { entries ->
                val size = entries.size

                val firstHalf = entries.toTypedArray().copyOfRange(0, (size + 1) / 2)
                val secondHalf = entries.toTypedArray().copyOfRange((size + 1) / 2, size)

                val firstAverage = firstHalf.toMutableList().sumOf { it.value } / firstHalf.size.toDouble()
                val secondAverage = secondHalf.toMutableList().sumOf { it.value } / secondHalf.size.toDouble()

                val difference = (firstAverage - secondAverage).round(2)
                val sign = if (firstAverage > secondAverage) "+" else ""

                return if (difference != 0.0) ("$sign${difference}") else ""
            }
        }
        return null
    }
}