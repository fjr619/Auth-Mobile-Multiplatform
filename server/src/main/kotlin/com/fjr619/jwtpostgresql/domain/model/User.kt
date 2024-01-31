package com.fjr619.jwtpostgresql.domain.model


import com.fjr619.jwtpostgresql.base.BaseEntity
import com.fjr619.jwtpostgresql.domain.getNowUTC
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

    override val createdAt: LocalDateTime = getNowUTC(),
    override val updatedAt: LocalDateTime = getNowUTC(),
    override val deleted: Boolean = false
    ) : BaseEntity() {

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