package domain.usecase.user

import domain.repository.UserRepository

class UserLogin(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.login()
}