package com.fjr619.jwtpostgresql.domain.service.story

import com.fjr619.jwtpostgresql.domain.model.RequestError
import com.fjr619.jwtpostgresql.domain.model.Story
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.github.michaelbull.result.Result

interface StoryService {
    suspend fun findById(id: Long): Result<Story, RequestError>
    suspend fun add(dto: StoryCreatedDto): Result<Story, RequestError>
}