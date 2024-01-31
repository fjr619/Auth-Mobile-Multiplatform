package com.fjr619.jwtpostgresql.data.repository

import com.fjr619.jwtpostgresql.base.statement.updateReturning
import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.schemas.StoryTable
import com.fjr619.jwtpostgresql.data.db.schemas.UserTable
import com.fjr619.jwtpostgresql.domain.getNowUTC
import com.fjr619.jwtpostgresql.domain.model.PaginatedResult
import com.fjr619.jwtpostgresql.domain.model.entity.Story
import com.fjr619.jwtpostgresql.domain.model.mapper.toStory
import com.fjr619.jwtpostgresql.domain.repository.StoryRepository
import kotlinx.datetime.toJavaLocalDateTime
import mu.KLogger
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.koin.core.annotation.Singleton
import kotlin.math.ceil
import kotlin.math.roundToLong

@Singleton
class StoryRepositoryImpl(
    private val databaseFactory: DatabaseFactory,
    private val logger: KLogger,
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

    override suspend fun save(userId: Long, entity: Story): Story? =
        if (entity.id == Story.NEW_STORY) {
            create(userId, entity)
        } else {
            update(userId, entity)
        }

    override suspend fun getList(userId: Long, page: Int, limit: Int): PaginatedResult<Story> {
        var pageCount: Long = 0
        var nextPage: Long? = null

        val stories = databaseFactory.dbQuery {
            (UserTable innerJoin StoryTable)
                .selectAll()
                .where {
                    (StoryTable.userId eq userId)
                }.orderBy(StoryTable.createdAt, SortOrder.DESC)
                .also {
                    pageCount = ceil((it.count().toDouble() / limit.toDouble())).toLong()
                    if (page < pageCount) {
                        nextPage = page + 1L
                    }
                }.limit(limit, (limit * (page-1)).toLong())
                .mapNotNull {
                    it.toStory(isWithUser = true)
                }
        }

        return PaginatedResult(pageCount, nextPage, stories)
    }

    private suspend fun create(userId: Long, entity: Story): Story? {
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


    private suspend fun update(userId: Long, entity: Story): Story? {
        return databaseFactory.dbQuery {
            StoryTable.updateReturning(
                where = {
                    (StoryTable.id eq entity.id) and (StoryTable.userId eq userId)
                },
                body = {
                    it[StoryTable.userId] = userId
                    it[title] = entity.title
                    it[content] = entity.content
                    it[isDraft] = entity.isDraft
                    it[updateAt] = getNowUTC().toJavaLocalDateTime()
                }
            ).map {
                it.toStory(isWithUser = false)
            }.singleOrNull()
        }
    }
}