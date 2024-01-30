package com.fjr619.jwtpostgresql.data.repository.auth

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.UserEntity
import com.fjr619.jwtpostgresql.data.db.UserTable
import com.fjr619.jwtpostgresql.data.db.convertTimeZone
import com.fjr619.jwtpostgresql.data.mapper.toModel
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.params.UserRegisterParams
import com.fjr619.jwtpostgresql.domain.repository.auth.AuthRepository
import com.fjr619.jwtpostgresql.domain.service.security.hash.HashingService
import com.fjr619.jwtpostgresql.domain.service.security.hash.SaltedHash
import com.fjr619.jwtpostgresql.presentation.plugin.ParsingException
import com.fjr619.jwtpostgresql.presentation.plugin.ValidationException
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import mu.KLogger
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.koin.core.annotation.Singleton
import java.time.ZoneOffset

/**
 * Auth Repository
 * @property databaseFactory Database service
 * @property hashingService Hashing service
 */

@Singleton
class AuthRepositoryImpl constructor(
    private val hashingService: HashingService,
    private val databaseFactory: DatabaseFactory,
    private val logger: KLogger
) : AuthRepository {

    /**
     * Register user
     * @param params User register params
     * @return User
     */
    override suspend fun registerUser(params: UserRegisterParams): User {
        val user = findUserByEmail(params.email)

        if (user != null) {
            throw ValidationException("Email already registered")
        }

        var statement: InsertStatement<Number>? = null
        databaseFactory.dbQuery {
            val saltedHash = hashingService.generateSaltedHash(params.password)
            statement = UserTable.insert {
                it[email] = params.email
                it[password] = saltedHash.hash
                it[fullName] = params.fullName
                it[avatar] = params.avatar
                it[salt] = saltedHash.salt
            }
        }

        return rowToUserEntity(statement?.resultedValues?.get(0))?.toModel()
            ?: throw ParsingException("Error cant register")
    }

    /**
     * Login User
     * @param email Email
     * @param password Password
     * @return User
     */
    override suspend fun loginUser(email: String, password: String): User {
        val user = findUserByEmail(email) ?: throw ValidationException("Incorrect username")

        //example
        logger.debug {
            "user time = ${user.createdAt.convertTimeZone(TimeZone.currentSystemDefault())}"
        }

        val isValidPassword = hashingService.verify(
            value = password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) throw ValidationException("Incorrect password")
        return user.toModel()
    }

    /**
     * Find User By Email
     * @param email Email
     * @return User Entity nullable
     */
    private suspend fun findUserByEmail(email: String): UserEntity? {
        val user = try {
            databaseFactory.dbQuery {
                UserTable.selectAll().where { UserTable.email.eq(email) }.map {
                    rowToUserEntity(it)
                }.singleOrNull()
            }
        } catch (e: Exception) {
            throw ParsingException(e.message!!)
        }

        return user
    }

    /**
     * Convert result to User Entity
     * @param row Result Row nullable
     * @return User Entity nullable
     */
    private fun rowToUserEntity(row: ResultRow?): UserEntity? {
        return if (row == null) null
        else UserEntity(
            id = row[UserTable.id],
            fullName = row[UserTable.fullName],
            avatar = row[UserTable.avatar],
            email = row[UserTable.email],
            salt = row[UserTable.salt],
            password = row[UserTable.password],
            role = row[UserTable.role],

            createdAt = row[UserTable.createdAt].toString().toLocalDateTime(),
            updatedAt = row[UserTable.updateAt].toString().toLocalDateTime(),
            deleted = row[UserTable.deleted]
        )
    }
}