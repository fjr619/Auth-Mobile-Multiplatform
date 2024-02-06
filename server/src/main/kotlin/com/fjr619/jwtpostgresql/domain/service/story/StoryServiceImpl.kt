package com.fjr619.jwtpostgresql.domain.service.story

import GENERIC_ERROR
import NOT_FOUND
import com.fjr619.jwtpostgresql.domain.model.PaginatedResult
import com.fjr619.jwtpostgresql.domain.model.RequestError
import com.fjr619.jwtpostgresql.domain.model.entity.Story
import com.fjr619.jwtpostgresql.domain.model.entity.User
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryUpdateDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toStory
import com.fjr619.jwtpostgresql.domain.repository.StoryRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.koin.core.annotation.Singleton

@Singleton
class StoryServiceImpl(
    private val storyRepository: StoryRepository,
): StoryService {
    override suspend fun findById(id: Long): Result<Story, RequestError> {
        if (id == -1L) return Err(RequestError.NotFound(NOT_FOUND))
        return storyRepository.findById(id)?.let {
            Ok(it)
        } ?: Err(RequestError.NotFound(NOT_FOUND))
    }

    override suspend fun add(user: User, params: StoryCreatedDto): Result<Story, RequestError> {
        return storyRepository.save(user.id, params.toStory())?.let {
            Ok(it.copy(
                user = user
            ))
        } ?: Err(RequestError.BadRequest(GENERIC_ERROR))
    }

    override suspend fun update(user: User, id: Long, params: StoryUpdateDto): Result<Story, RequestError> {
        return storyRepository.save(user.id, params.toStory().copy(id = id))?.let {
            Ok(it.copy(
                user = user
            ))
        } ?: Err(RequestError.BadRequest(GENERIC_ERROR))
    }

    override suspend fun getList(
        userId: Long,
        page: Int,
        limit: Int
    ): Result<PaginatedResult<Story>, RequestError> {
        return Ok(storyRepository.getList(userId, page, limit))
    }

    override suspend fun delete(userId: Long, id: Long): Result<Boolean, RequestError> {
        if (id == -1L) return Err(RequestError.NotFound(NOT_FOUND))
        return if (storyRepository.delete(userId, id)) {
            Ok(true)
        } else {
            Err(RequestError.NotFound(NOT_FOUND))
        }
    }
}