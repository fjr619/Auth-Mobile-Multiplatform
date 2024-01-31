package com.fjr619.jwtpostgresql.domain.model.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class StoryDto(
    val id: Long,
    val title: String,
    val content: String,
    val isDraft: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val user: UserDto? = null,
)

@Serializable
data class StoryCreatedDto(
    val title: String,
    val content: String,
    val isDraft: Boolean
)

@Serializable
data class StoryUpdateDto(
    val title: String,
    val content: String,
    val isDraft: Boolean
)