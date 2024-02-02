package com.fjr619.jwtpostgresql.data.db.schemas

import com.fjr619.jwtpostgresql.base.BaseTable

object StoryTable: BaseTable("stories") {
    val id = long("di").autoIncrement()
    val userId = (long("user_id")) references(UserTable.id)
    val title = varchar("title", 256)
    val content = text("content")
    val isDraft = bool("is_draft").clientDefault { true }
    override val primaryKey = PrimaryKey(id)
}