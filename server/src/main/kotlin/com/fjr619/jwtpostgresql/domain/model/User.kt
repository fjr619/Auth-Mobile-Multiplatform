package com.fjr619.jwtpostgresql.domain.model


import com.fjr619.jwtpostgresql.domain.model.User.Role.ADMIN
import com.fjr619.jwtpostgresql.domain.model.User.Role.USER
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


data class User(
    val id: Long = NEW_USER,

    //data
    val fullName: String,
    val avatar: String = DEFAULT_IMAGE,
    val email: String,
    val password: String,
    val salt: String = "",
    val role: Role = USER,

    //metadata
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val deleted: Boolean = false
) {

    /**
     * Companion object
     * @property NEW_USER New user ID
     * @property DEFAULT_IMAGE Default image URL
     */
    companion object {
        const val NEW_USER = -1L
        const val DEFAULT_IMAGE = "https://i.imgur.com/fIgch2x.png"
    }

    /**
     * User roles
     * @property USER Normal user
     * @property ADMIN Administrator user
     */
    enum class Role {
        USER, ADMIN
    }
}

/**
 * User Errors
 */
sealed class UserError(val message: String) {
    class NotFound(message: String) : UserError(message)
    class BadRequest(message: String) : UserError(message)
    class BadCredentials(message: String) : UserError(message)
    class BadRole(message: String) : UserError(message)
    class Unauthorized(message: String) : UserError(message)
    class Forbidden(message: String) : UserError(message)
}

fun LocalDateTime.convertTimeZone(zone: TimeZone): LocalDateTime {
    return this.toInstant(TimeZone.UTC).toLocalDateTime(zone)
}