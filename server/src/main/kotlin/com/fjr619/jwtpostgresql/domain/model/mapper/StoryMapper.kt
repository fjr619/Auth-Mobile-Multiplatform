package com.fjr619.jwtpostgresql.domain.model.mapper

import com.fjr619.jwtpostgresql.data.db.schemas.StoryTable
import com.fjr619.jwtpostgresql.data.db.schemas.UserTable
import com.fjr619.jwtpostgresql.domain.model.Story
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryDto
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ResultRow

fun Story.toDto() = StoryDto(
    id = this.id,
    user = this.user?.toDto(),
    title = this.title,
    content = this.content,
    isDraft = this.isDraft,
    createdAt = this.createdAt
)

fun StoryCreatedDto.toStory() = Story(
    title = this.title,
    content = this.content,
    isDraft = this.isDraft
)

fun ResultRow?.toStory(isWithUser: Boolean = false): Story? {
    return if (this == null) null
    else Story(
        id = this[StoryTable.id],
        title = this[StoryTable.title],
        content = this[StoryTable.content],
        isDraft = this[StoryTable.isDraft],
        createdAt  = this[StoryTable.createdAt].toString().toLocalDateTime(),
        user = if (isWithUser) this.toUser() else null
    )
}