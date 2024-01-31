package com.fjr619.jwtpostgresql.domain.repository

import com.fjr619.jwtpostgresql.domain.model.PaginatedResult
import com.fjr619.jwtpostgresql.domain.model.entity.Story

interface StoryRepository {
    suspend fun findById(id: Long): Story?
    suspend fun save(userId: Long, entity: Story): Story?
    suspend fun getList(userId: Long, page: Int, limit: Int = 10): PaginatedResult<Story>

    suspend fun delete(userId: Long, id: Long): Boolean
}