package domain.usecase.user

import domain.repository.UserRepository

class UserRegister(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.register()
}