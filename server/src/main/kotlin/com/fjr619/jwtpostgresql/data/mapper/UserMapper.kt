package com.fjr619.jwtpostgresql.data.mapper

import com.fjr619.jwtpostgresql.data.db.UserEntity
import com.fjr619.jwtpostgresql.domain.model.User

fun UserEntity.toModel() = User(
    id = this.id,
    fullName = this.fullName,
    avatar = this.avatar,
    email = this.email
)

//fun User.toEntity() = UserEntity(
//    id, fullName, avatar, email, password, salt, createdAt
//)