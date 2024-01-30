package com.fjr619.jwtpostgresql.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Story(
    val id: Long = Story.NEW_STORY,
    val user: User? = null,
    val title: String,
    val content: String,
    val isDraft: Boolean = true,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
) {
    companion object {
        const val NEW_STORY = -1L
    }
}

sealed class StoryError(val message: String) {
    class NotFound(message: String) : StoryError(message)
    class BadRequest(message: String) : StoryError(message)
}