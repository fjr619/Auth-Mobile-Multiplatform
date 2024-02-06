package domain.usecase.user

import domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UserLogin(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.login()
}