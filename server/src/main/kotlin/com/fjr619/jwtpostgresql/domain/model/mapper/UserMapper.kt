package com.fjr619.jwtpostgresql.domain.model.mapper

import com.fjr619.jwtpostgresql.data.db.UserTable
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ResultRow

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        fullName = this.fullName,
        avatar = this.avatar,
        email = this.email,
        role = this.role
    )
}

fun UserCreateDto.toModel(): User {
    return User(
        fullName = this.fullName,
        avatar = this.avatar,
        email = this.email,
        password = this.password
    )
}
fun ResultRow?.toModel(): User? {
    return if (this == null) null
    else User(
        id = this[UserTable.id],
        fullName = this[UserTable.fullName],
        avatar = this[UserTable.avatar],
        email = this[UserTable.email],
        salt = this[UserTable.salt],
        password = this[UserTable.password],
        role = User.Role.valueOf(this[UserTable.role]),

        createdAt = this[UserTable.createdAt].toString().toLocalDateTime(),
        updatedAt = this[UserTable.updateAt].toString().toLocalDateTime(),
        deleted = this[UserTable.deleted]
    )
}