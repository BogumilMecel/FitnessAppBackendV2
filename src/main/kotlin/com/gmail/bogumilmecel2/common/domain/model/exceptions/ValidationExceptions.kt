package com.gmail.bogumilmecel2.common.domain.model.exceptions

import com.github.aymanizz.ktori18n.R

data object InvalidIdException : BadRequestException(resource = R("invalid_id"))
data object InvalidNutritionValuesException : BadRequestException(resource = R("invalid_nutrition_values"))
data object InvalidNutritionValuesInException : BadRequestException(resource = R("invalid_nutrition_values_in"))
data object InvalidWeightException : BadRequestException(resource = R("invalid_weight"))
data object InvalidMeasurementUnit : BadRequestException(resource = R("invalid_measurement_unit"))
data object InvalidBarcodeLengthException : BadRequestException(resource = R("invalid_barcode_length"))
data object InvalidProductNameException : BadRequestException(resource = R("invalid_product_name"))
data object InvalidDateException : BadRequestException(resource = R("invalid_date"))
data object InvalidMealNameException : BadRequestException(resource = R("invalid_meal_name"))
data object InvalidServingsException : BadRequestException(resource = R("invalid_servings"))

data object CouldNotFindUserException : NotFoundException(resource = R("could_not_find_username"))
data object DiaryEntryNotFoundException: NotFoundException(resource = R("diary_entry_not_found"))