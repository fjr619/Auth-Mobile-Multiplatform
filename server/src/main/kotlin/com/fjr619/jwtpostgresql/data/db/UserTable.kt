package com.fjr619.jwtpostgresql.data.db

import com.fjr619.jwtpostgresql.domain.model.User
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : Table("users") {
    val id = long(User::id.name).autoIncrement()

    //data
    val fullName = varchar(User::fullName.name, 256)
    val avatar = text(User::avatar.name)
    val email = varchar(User::email.name, 256)
    val password = text(User::password.name)
    val salt = text(User::salt.name)
    val role = varchar(User::role.name, 256).clientDefault { User.Role.USER.name }

    //metadata
    val createdAt = datetime(User::createdAt.name).clientDefault {
        (Clock.System.now().toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime())
    }
    val updateAt = datetime(User::updatedAt.name).clientDefault {
        Clock.System.now().toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
    }
    val deleted = bool(User::deleted.name).clientDefault { false }

    override val primaryKey = PrimaryKey(id)
}