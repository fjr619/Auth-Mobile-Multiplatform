package data.mapper

import domain.model.User
import domain.model.UserLogin
import dto.UserDto
import dto.UserLoginDto

fun UserDto.toModel(): User = User(
    id, fullName, avatar, email, createdAt, updatedAt
)

fun UserLogin.toDto(): UserLoginDto = UserLoginDto(
    email, password
)