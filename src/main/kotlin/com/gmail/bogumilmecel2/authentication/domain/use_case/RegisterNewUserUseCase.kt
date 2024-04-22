package com.gmail.bogumilmecel2.authentication.domain.use_case

import com.gmail.bogumilmecel2.authentication.domain.model.AuthResponse
import com.gmail.bogumilmecel2.authentication.domain.model.token.TokenConfig
import com.gmail.bogumilmecel2.authentication.domain.model.user.UserDto
import com.gmail.bogumilmecel2.authentication.domain.service.HashingService
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.common.util.copyType
import com.gmail.bogumilmecel2.user.user_data.domain.repository.UserRepository
import org.bson.types.ObjectId

class RegisterNewUserUseCase(
    private val userRepository: UserRepository,
    private val hashingService: HashingService,
    private val checkIfUsernameExists: CheckIfUsernameExists,
    private val getUserByUsernameUseCase: GetUserByUsernameUseCase
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        tokenConfig: TokenConfig
    ): Resource<AuthResponse> {
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

                when(val resource = userRepository.registerNewUser(user = user)) {
                    is Resource.Success -> {
                        getUserByUsernameUseCase(
                            email = email,
                            password = password,
                            tokenConfig = tokenConfig
                        )
                    }
                    is Resource.Error -> resource.copyType()
                }
            } else {
                Resource.Error()
            }
        }
    }
}