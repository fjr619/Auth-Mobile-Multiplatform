package di

import domain.repository.UserRepository
import domain.usecase.user.UserUseCases
import domain.usecase.user.UserLogin
import domain.usecase.user.UserRegister
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module{
//    factory { UserLogin(get()) }
//
//    factory { UserRegister(get()) }
//
//    factory { UserUseCases(get(), get()) }
}