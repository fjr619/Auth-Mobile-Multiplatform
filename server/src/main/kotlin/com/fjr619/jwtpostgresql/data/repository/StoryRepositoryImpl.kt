package com.fjr619.jwtpostgresql.data.repository

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.schemas.StoryTable
import com.fjr619.jwtpostgresql.data.db.schemas.UserTable
import com.fjr619.jwtpostgresql.domain.model.Story
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toStory
import com.fjr619.jwtpostgresql.domain.repository.StoryRepository
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Singleton

@Singleton
class StoryRepositoryImpl(
    private val databaseFactory: DatabaseFactory,
) : StoryRepository {
    override suspend fun findById(id: Long): Story? {
        return databaseFactory.dbQuery {
            (UserTable innerJoin StoryTable)
                .selectAll()
                .where {
                    StoryTable.id eq id
                }.map {
                    it.toStory()
                }.singleOrNull()
        }
    }

    override suspend fun add(dto: StoryCreatedDto): Story? {
        TODO("Not yet implemented")
    }
}