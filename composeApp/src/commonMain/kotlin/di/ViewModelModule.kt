package di

import org.koin.dsl.module
import ui.login.UserLoginViewModel

val viewModelModule = module{
    factory {
        UserLoginViewModel(get())
    }
}