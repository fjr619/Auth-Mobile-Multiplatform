package com.fjr619.jwtpostgresql.domain.repository

import com.fjr619.jwtpostgresql.domain.model.Story
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto

interface StoryRepository {
    suspend fun findById(id: Long): Story?
    suspend fun add(dto: StoryCreatedDto): Story?
}