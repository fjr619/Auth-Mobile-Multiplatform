package com.fjr619.jwtpostgresql.domain.model.entity

import com.fjr619.jwtpostgresql.base.BaseEntity
import com.fjr619.jwtpostgresql.domain.getNowUTC
import kotlinx.datetime.LocalDateTime

data class Story(
    val id: Long = NEW_STORY,
    val user: User? = null,
    val title: String,
    val content: String,
    val isDraft: Boolean = true,

    override val createdAt: LocalDateTime = getNowUTC(),
    override val updatedAt: LocalDateTime = getNowUTC(),
    override val deleted: Boolean = false
): BaseEntity {
    companion object {
        const val NEW_STORY = -1L
    }
}

sealed class StoryError(val message: String) {
    class NotFound(message: String) : StoryError(message)
    class BadRequest(message: String) : StoryError(message)
}