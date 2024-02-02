package domain.repository

import data.Response
import domain.model.User

interface UserRepository {
    suspend fun login(): Response<User>
}