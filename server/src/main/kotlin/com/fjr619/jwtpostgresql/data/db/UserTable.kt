package com.fjr619.jwtpostgresql.data.db

import com.fjr619.jwtpostgresql.domain.model.User
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : Table("users") {
    val id = long(UserEntity::id.name).autoIncrement()

    //data
    val fullName = varchar(UserEntity::fullName.name, 256)
    val avatar = text(UserEntity::avatar.name)
    val email = varchar(UserEntity::email.name, 256)
    val password = text(UserEntity::password.name)
    val salt = text(UserEntity::salt.name)
    val role = varchar(UserEntity::role.name, 256).clientDefault { User.Role.USER.name }

    //metadata
    val createdAt = datetime(UserEntity::createdAt.name).clientDefault {
        (Clock.System.now().toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime())
    }
    val updateAt = datetime(UserEntity::updatedAt.name).clientDefault {
        Clock.System.now().toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
    }
    val deleted = bool(UserEntity::deleted.name).clientDefault { false }

    override val primaryKey = PrimaryKey(id)
}

data class UserEntity(
    val id: Long,

    //data
    val fullName: String,
    val avatar: String,
    val email: String,
    val password: String,
    val salt: String,
    val role: String = User.Role.USER.name,

    //metadata
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val deleted: Boolean = false
)

fun LocalDateTime.convertTimeZone(zone: TimeZone): LocalDateTime {
    return this.toInstant(TimeZone.UTC).toLocalDateTime(zone)
}