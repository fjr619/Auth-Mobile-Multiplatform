package com.fjr619.jwtpostgresql.domain.model

import com.fjr619.jwtpostgresql.domain.model.User.Role.ADMIN
import com.fjr619.jwtpostgresql.domain.model.User.Role.USER
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable


/**
 * User Model
 * @param id User ID
 * @param fullName User name
 * @param email User email
 * @param password User password
 * @param avatar User avatar URL
 * @param role User role
 */

@Serializable
data class User(
    val id: Long,
    val fullName: String,
    val avatar: String,
    val email: String,
    var authToken: String? = null,
    val role: Role = Role.USER
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