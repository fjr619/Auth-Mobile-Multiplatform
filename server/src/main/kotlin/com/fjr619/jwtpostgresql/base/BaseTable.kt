package com.fjr619.jwtpostgresql.base

import com.fjr619.jwtpostgresql.domain.getNowUTC
import com.fjr619.jwtpostgresql.domain.model.entity.User
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

open class BaseTable(name: String): Table(name) {
    open val createdAt = datetime("created_at").clientDefault {
        getNowUTC().toJavaLocalDateTime()
    }
    open val updateAt = datetime("updated_at").clientDefault {
        getNowUTC().toJavaLocalDateTime()
    }

    open val deleted = bool(User::deleted.name).clientDefault { false }
}

interface BaseEntity {
    val createdAt: LocalDateTime
    val updatedAt: LocalDateTime
    val deleted: Boolean
}