package com.fjr619.jwtpostgresql.data.repository

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.schemas.StoryTable
import com.fjr619.jwtpostgresql.data.db.schemas.UserTable
import com.fjr619.jwtpostgresql.domain.model.Story
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toStory
import com.fjr619.jwtpostgresql.domain.model.mapper.toUser
import com.fjr619.jwtpostgresql.domain.repository.StoryRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
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
                    it.toStory(isWithUser = true)
                }.singleOrNull()
        }
    }

    override suspend fun add(userId: Long, entity: Story): Story? {
        var statement: InsertStatement<Number>? = null
        databaseFactory.dbQuery {
            statement = StoryTable.insert {
                it[StoryTable.userId] = userId
                it[title] = entity.title
                it[content] = entity.content
                it[isDraft] = entity.isDraft
            }
        }
        return statement?.resultedValues?.get(0).toStory(isWithUser = false)
    }
}