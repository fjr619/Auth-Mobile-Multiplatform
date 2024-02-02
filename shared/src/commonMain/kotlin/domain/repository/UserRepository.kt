package domain.repository

import data.model.Response
import data.model.UserDto
import domain.model.User
import domain.model.UserLogin
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(): Result<Response<User>>
}