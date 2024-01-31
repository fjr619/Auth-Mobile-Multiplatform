package com.fjr619.jwtpostgresql.data.db.schemas

import com.fjr619.jwtpostgresql.base.BaseTable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object StoryTable: BaseTable("stories") {
    val id = long("id").autoIncrement()
    val userId = (long("user_id")) references(UserTable.id)
    val title = varchar("title", 256)
    val content = text("content")
    val isDraft = bool("is_draft").clientDefault { true }
    override val primaryKey = PrimaryKey(id)
}