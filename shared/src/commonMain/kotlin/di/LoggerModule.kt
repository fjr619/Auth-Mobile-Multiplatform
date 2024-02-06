package di

import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.dsl.module

val loggerModule = module {
    single {
        KotlinLogging.logger {}
    }
}