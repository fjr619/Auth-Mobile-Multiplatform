package di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

fun initKoin() = startKoin {
    defaultModule()
    modules(
        networkModule,
        loggerModule
//        repositoryModule,
//        useCaseModule,
//        viewModelModule
    )
}