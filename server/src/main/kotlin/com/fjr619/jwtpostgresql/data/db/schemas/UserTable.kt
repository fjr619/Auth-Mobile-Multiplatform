package com.fjr619.jwtpostgresql.data.db.schemas

import com.fjr619.jwtpostgresql.base.BaseTable
import com.fjr619.jwtpostgresql.domain.model.entity.User

object UserTable : BaseTable("users") {
    val id = long(User::id.name).autoIncrement()

    //data
    val fullName = varchar(User::fullName.name, 256)
    val avatar = text(User::avatar.name)
    val email = varchar(User::email.name, 256)
    val password = text(User::password.name)
    val salt = text(User::salt.name)
    val role = varchar(User::role.name, 256).clientDefault { User.Role.USER.name }
    override val primaryKey = PrimaryKey(id)
}