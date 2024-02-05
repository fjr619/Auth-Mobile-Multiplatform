package domain.repository

import Response
import domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(): Flow<Response<User>>
}