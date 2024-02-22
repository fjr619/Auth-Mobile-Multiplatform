package data.repository

import Response
import data.mapper.toModel
import di.RequestException
import domain.model.User
import domain.repository.UserRepository
import dto.UserCreateDto
import dto.UserDto
import dto.UserLoginDto
import io.github.oshai.kotlinlogging.KLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory


@Factory
class UserRepositoryImpl(
    private val apiService: HttpClient,
    private val logger: KLogger
) : UserRepository {
    override suspend fun login(): Flow<Response<User>> {
        return flow {
            try {
                val response = apiService.post("user/login") {
                    setBody(
                        UserLoginDto(
                            email = "multi1@aaa.com",
                            password = "1234"
                        )
                    )
                }

                val temp: Response<UserDto> = response.body()
                logger.info {"zzzz SuccessResponse"}
                emit(
                    Response.SuccessResponse(
                        data = temp.data?.toModel(),
                        token = temp.token,
                        statusCode = temp.statusCode,
                        message = temp.message
                    ).toResponse()
                )
            } catch (e: RequestException) {
                logger.info {"zzzz ErrorResponse ${e.message} ${e.statusCode}"}
                emit(Response.ErrorResponse(message = e.message, statusCode = e.statusCode))
            }
        }
    }

    override suspend fun register(): Flow<Response<User>> {
        return flow {
            try {
                val response = apiService.post("user/register") {
                    setBody(
                        UserCreateDto(
                            email = "multi@aaa.com",
                            password = "1234",
                            fullName = "From Multiplatform",
                            avatar = "avatar"
                        )
                    )
                }

                val temp: Response<UserDto> = response.body()

                emit(temp.data?.let { dto ->
                    Response.SuccessResponse(
                        data = dto.toModel(),
                        token = temp.token,
                        statusCode = temp.statusCode,
                        message = temp.message
                    ).toResponse()
                } ?: Response.ErrorResponse(
                    statusCode = temp.statusCode,
                    message = temp.message
                ).toResponse())
            } catch (e: RequestException) {
                logger.info{"zzzz Exception $e.message"}
                emit(Response.ErrorResponse(message = e.message, statusCode = e.statusCode))
            }
        }
    }
}