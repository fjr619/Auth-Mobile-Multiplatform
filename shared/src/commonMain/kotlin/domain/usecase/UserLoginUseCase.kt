package domain.usecase

import domain.model.UserLogin
import domain.repository.UserRepository

class UserLoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.login()
}