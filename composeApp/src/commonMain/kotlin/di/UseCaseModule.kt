package di

import domain.usecase.UserLoginUseCase
import org.koin.dsl.module

val useCaseModule = module{
    single {
        UserLoginUseCase(get())
    }
}