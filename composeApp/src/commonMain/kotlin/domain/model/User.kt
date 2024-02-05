package domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

data class User(
    val id: Long,
    val fullName: String,
    val avatar: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class UserLogin(
    val email: String,
    val password: String
)