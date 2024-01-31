package com.fjr619.jwtpostgresql.domain.repository

import com.fjr619.jwtpostgresql.domain.model.entity.Story

interface StoryRepository {
    suspend fun findById(id: Long): Story?
    suspend fun save(userId: Long, entity: Story): Story?
}