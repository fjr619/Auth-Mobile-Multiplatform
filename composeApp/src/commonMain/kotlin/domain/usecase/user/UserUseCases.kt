package domain.usecase.user

import org.koin.core.annotation.Factory

@Factory
data class UserUseCases(
    val userLogin: UserLogin,
    val userRegister: UserRegister
){
}