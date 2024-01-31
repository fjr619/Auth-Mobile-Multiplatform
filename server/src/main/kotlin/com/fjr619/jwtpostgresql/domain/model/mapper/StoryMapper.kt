package com.fjr619.jwtpostgresql.domain.model.mapper

import com.fjr619.jwtpostgresql.data.db.schemas.StoryTable
import com.fjr619.jwtpostgresql.domain.model.entity.Story
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryUpdateDto
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ResultRow

fun Story.toDto() = StoryDto(
    id = this.id,
    user = this.user?.toDto(),
    title = this.title,
    content = this.content,
    isDraft = this.isDraft,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString()
)

fun StoryCreatedDto.toStory() = Story(
    title = this.title,
    content = this.content,
    isDraft = this.isDraft
)

fun StoryUpdateDto.toStory() = Story(
    id = this.id,
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
        updatedAt = this[StoryTable.updateAt].toString().toLocalDateTime(),
        deleted = this[StoryTable.deleted],
        user = if (isWithUser) this.toUser() else null
    )
}