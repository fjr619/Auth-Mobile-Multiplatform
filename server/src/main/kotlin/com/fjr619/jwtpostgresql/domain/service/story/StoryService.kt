package com.fjr619.jwtpostgresql.domain.service.story

import com.fjr619.jwtpostgresql.domain.model.RequestError
import com.fjr619.jwtpostgresql.domain.model.entity.Story
import com.fjr619.jwtpostgresql.domain.model.entity.User
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryUpdateDto
import com.github.michaelbull.result.Result

interface StoryService {
    suspend fun findById(id: Long): Result<Story, RequestError>
    suspend fun add(user: User, params: StoryCreatedDto): Result<Story, RequestError>

    suspend fun update(user: User, params: StoryUpdateDto): Result<Story, RequestError>
}