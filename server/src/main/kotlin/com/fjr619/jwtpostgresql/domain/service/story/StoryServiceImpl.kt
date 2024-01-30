package com.fjr619.jwtpostgresql.domain.service.story

import com.fjr619.jwtpostgresql.domain.model.GENERIC_ERROR
import com.fjr619.jwtpostgresql.domain.model.NOT_FOUND
import com.fjr619.jwtpostgresql.domain.model.RequestError
import com.fjr619.jwtpostgresql.domain.model.Story
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toStory
import com.fjr619.jwtpostgresql.domain.repository.StoryRepository
import com.fjr619.jwtpostgresql.domain.repository.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import org.koin.core.annotation.Singleton

@Singleton
class StoryServiceImpl(
    private val storyRepository: StoryRepository,
): StoryService {
    override suspend fun findById(id: Long): Result<Story, RequestError> {
        return storyRepository.findById(id)?.let {
            Ok(it)
        } ?: Err(RequestError.NotFound(NOT_FOUND))
    }

    override suspend fun add(user: User, params: StoryCreatedDto): Result<Story, RequestError> {
        return storyRepository.add(user.id, params.toStory())?.let {
            Ok(it.copy(
                user = user
            ))
        } ?: Err(RequestError.BadRequest(GENERIC_ERROR))
    }
}