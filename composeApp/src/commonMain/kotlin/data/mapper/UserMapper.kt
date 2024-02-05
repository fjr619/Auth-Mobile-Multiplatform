package data.mapper

import data.model.UserDto
import data.model.UserLoginDto
import domain.model.User
import domain.model.UserLogin

fun UserDto.toModel() : User = User(
    id, fullName, avatar, email, createdAt, updatedAt
)

fun UserLogin.toDto() : UserLoginDto = UserLoginDto(
    email, password
)