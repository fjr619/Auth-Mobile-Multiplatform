package data.repository

import Response
import data.mapper.toModel
import domain.model.User
import domain.repository.UserRepository
import dto.UserCreateDto
import dto.UserDto
import dto.UserLoginDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class UserRepositoryImpl(
    private val apiService: HttpClient
) : UserRepository {
    override suspend fun login(): Flow<Response<User>> {
        return flow {
            try {
                val response = apiService.post("user/login") {
                    setBody(
                        UserLoginDto(
                            email = "aaa@aaa.com",
                            password = "1234"
                        )
                    )
                }

                val temp: Response<UserDto> = response.body()
                emit(
                    Response.SuccessResponse(
                        data = temp.data?.toModel(),
                        token = temp.token,
                        statusCode = temp.statusCode,
                        message = temp.message
                    ).toResponse()
                )

            } catch (e: Exception) {
                emit(Response.ErrorResponse(message = e.message))
            }
        }
    }

    override suspend fun register(): Flow<Response<User>> {
        return flow {
            try {
                val response = apiService.post("user/register") {
                    setBody(
                        UserCreateDto(
                            email = "and1@aaa.com",
                            password = "1234",
                            fullName = "From Android",
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
            } catch (e: Exception) {
                emit(Response.ErrorResponse(message = e.message))
            }
        }
    }
}