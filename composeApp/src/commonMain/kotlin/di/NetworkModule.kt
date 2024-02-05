package di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule  = module {
    single {
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
            }

            install(ContentNegotiation) {
                json(Json {
//                    ignoreUnknownKeys = true
                    prettyPrint = true
                }, contentType = ContentType.Any)
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }

            defaultRequest {
                url {
                    host = "192.168.68.71:8080/api"
                    contentType(ContentType.Application.Json)
                }
            }
        }
    }
}