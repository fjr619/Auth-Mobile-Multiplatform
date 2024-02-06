package com.fjr619.jwtpostgresql.data.repository

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.schemas.UserTable
import com.fjr619.jwtpostgresql.domain.model.mapper.toUser
import com.fjr619.jwtpostgresql.domain.model.entity.User
import com.fjr619.jwtpostgresql.domain.repository.UserRepository
import com.fjr619.jwtpostgresql.domain.service.security.hash.HashingService
import com.fjr619.jwtpostgresql.domain.service.security.hash.SaltedHash
import io.github.oshai.kotlinlogging.KLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.koin.core.annotation.Singleton

/**
 * Auth Repository
 * @property databaseFactory Database service
 * @property hashingService Hashing service
 */

@Singleton
class UserRepositoryImpl (
    private val hashingService: HashingService,
    private val databaseFactory: DatabaseFactory,
    private val logger: KLogger
) : UserRepository {

    override suspend fun findByEmail(email: String): User? {
        return databaseFactory.dbQuery {
            UserTable.selectAll().where { UserTable.email.eq(email) }.map {
                it.toUser()
            }.singleOrNull()
        }
    }

    override suspend fun findById(id: Long): User? {
        return databaseFactory.dbQuery {
            UserTable.selectAll().where { UserTable.id.eq(id) }.map {
                it.toUser()
            }.singleOrNull()
        }
    }

    override suspend fun save(entity: User): User? = if (entity.id == User.NEW_USER) {
        create(entity)
    } else {
        update(entity)
    }

    override suspend fun checkUserNameAndPassword(email: String, password: String): User? {
        return findByEmail(email)?.let {
            val isValidPassword = hashingService.verify(
                value = password,
                saltedHash = SaltedHash(
                    hash = it.password,
                    salt = it.salt
                )
            )
            if (isValidPassword) it else null
        }


    }

    private suspend fun create(entity: User): User? {
        var statement: InsertStatement<Number>? = null
        databaseFactory.dbQuery {
            val saltedHash = hashingService.generateSaltedHash(entity.password)
            statement = UserTable.insert {
                it[email] = entity.email
                it[password] = saltedHash.hash
                it[fullName] = entity.fullName
                it[avatar] = entity.avatar
                it[salt] = saltedHash.salt
            }
        }

        return statement?.resultedValues?.get(0).toUser()
    }

    private fun update(entity: User): User? {
        return null
    }
}