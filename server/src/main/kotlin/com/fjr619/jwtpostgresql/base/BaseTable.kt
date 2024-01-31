package com.fjr619.jwtpostgresql.base

import com.fjr619.jwtpostgresql.domain.getNowUTC
import com.fjr619.jwtpostgresql.domain.model.User
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
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

abstract class BaseEntity {
    abstract val createdAt: LocalDateTime
    abstract val updatedAt: LocalDateTime
    abstract val deleted: Boolean
}