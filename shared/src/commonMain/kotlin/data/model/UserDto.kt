package data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    val id: Long,
    val fullName: String,
    val avatar: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
//    var authToken: String? = null,
//    val role: User.Role = User.Role.USER
)

@Serializable
data class UserLoginDto(
    val email: String,
    val password: String
)