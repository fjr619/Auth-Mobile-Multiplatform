package com.fjr619.jwtpostgresql.domain.model.dto

import com.fjr619.jwtpostgresql.domain.model.User
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class StoryDto(
    val id: Long,
    val user: UserDto? = null,
    val title: String,
    val content: String,
    val isDraft: Boolean,
    val createdAt: LocalDateTime,
)

@Serializable
data class StoryCreatedDto(
    val title: String,
    val content: String,
    val isDraft: Boolean
)