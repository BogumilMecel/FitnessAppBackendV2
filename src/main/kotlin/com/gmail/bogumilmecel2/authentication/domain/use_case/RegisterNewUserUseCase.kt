package com.gmail.bogumilmecel2.authentication.domain.use_case

import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.authentication.domain.service.HashingService
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import org.bson.types.ObjectId

class RegisterNewUserUseCase(
    private val userRepository: UserRepository,
    private val hashingService: HashingService,
    private val checkIfUsernameExists: CheckIfUsernameExists
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Resource<Boolean> {
        return if (username.isBlank() || email.isBlank() || password.isBlank()) {
            Resource.Error()
        } else if (password.length < Constants.Authentication.PASSWORD_MIN_LENGTH || password.length > Constants.Authentication.PASSWORD_MAX_LENGTH) {
            Resource.Error()
        } else if (username.length < Constants.Authentication.USERNAME_MIN_LENGTH || username.length > Constants.Authentication.USERNAME_MAX_LENGTH) {
            Resource.Error()
        } else {
            val usernameExistsResource = checkIfUsernameExists(
                username = username
            )
            if (usernameExistsResource.data == true) {
                val saltedHash = hashingService.generateSaltedHash(password)

                val user = UserDto(
                    _id = ObjectId(),
                    username = username,
                    email = email,
                    password = saltedHash.hash,
                    salt = saltedHash.salt,
                )

                userRepository.registerNewUser(
                    user = user
                )
            } else {
                Resource.Error()
            }
        }
    }
}