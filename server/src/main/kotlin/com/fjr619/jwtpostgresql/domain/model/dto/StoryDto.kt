package com.fjr619.jwtpostgresql.domain.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class StoryDto(
    val id: Long,
    val title: String,
    val content: String,
    val isDraft: Boolean,
    val createdAt: String,
    val updatedAt: String,
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
    val id: Long,
    val title: String,
    val content: String,
    val isDraft: Boolean
)