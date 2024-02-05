package dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * User DTO for response
 */
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

/**
 * User DTO for request to create a new user
 */
@Serializable
data class UserCreateDto(
    val fullName: String,
    val email: String,
    val password: String,
    val avatar: String
)

/**
 * User DTO for request to login a user
 */
@Serializable
data class UserLoginDto(
    val email: String,
    val password: String
)