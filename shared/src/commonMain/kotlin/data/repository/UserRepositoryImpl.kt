package data.repository

import data.mapper.toModel
import data.model.Response
import data.model.UserDto
import data.model.UserLoginDto
import domain.model.User
import domain.model.UserLogin
import domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UserRepositoryImpl constructor(
    private val apiService: HttpClient
) : UserRepository {
    override suspend fun login(): Result<Response<User>> {
        return try {
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
            if (temp.data != null) {
                val response2: Response<User> = Response(
                    data = temp.data.toModel(),
                    type = temp.type,
                    token = temp.token,
                    statusCode = temp.statusCode,
                    message = temp.message
                )
                Result.success(response2)
            } else {
                val response2: Response<User> = Response(
                    type = temp.type,
                    token = temp.token,
                    message = temp.message
                )
                Result.failure(Exception(response2.message))
            }

//            if (response.status == HttpStatusCode.OK || response.status ==HttpStatusCode.Created) {
//                val temp: Response<UserDto> = response.body()
//                val response2: Response<User> = Response(
//                    data = temp.data?.toModel(),
//                    type = temp.type,
//                    token = temp.token,
//                    statusCode = temp.statusCode,
//                    message = temp.message
//                )
//                Result.success(response2)
//            } else {
//                Result.failure<Nothing>(Throwable("${response.status}: ${response.bodyAsText()}"))
//            }

        } catch (e: Exception) {
            println("aaa Exception")
            Result.failure<Nothing>(e)
        }

    }
}