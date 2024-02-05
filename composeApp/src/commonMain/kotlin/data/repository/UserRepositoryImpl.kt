package data.repository

import Response
import data.mapper.toModel
import data.model.UserDto
import data.model.UserLoginDto
import domain.model.User
import domain.repository.UserRepository
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
                    contentType(ContentType.Application.Json)
                    setBody(
                        UserLoginDto(
                            email = "aaa@aaa.com",
                            password = "1234"
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
                println("aaa Exception")
                emit(Response.ErrorResponse(message = e.message))
            }
        }
    }
}