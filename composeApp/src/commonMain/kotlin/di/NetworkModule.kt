package di

import Response
import domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
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
            expectSuccess = true
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }, contentType = ContentType.Application.Json)
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, request ->
                    when(exception) {
                        is ClientRequestException -> {
                            val a = exception.response.body<Response<Nothing>>()
                            throw RequestException(statusCode = a.statusCode, message = a.message)
                        }
                    }
                }
            }

            defaultRequest {
                url("http://192.168.68.71:8080/api/")
                contentType(ContentType.Application.Json)
            }
        }
    }
}

class RequestException (override val message: String?, val statusCode: Int) : Exception(message)
