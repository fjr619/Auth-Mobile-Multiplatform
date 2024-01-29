package com.fjr619.jwtpostgresql.data.repository.auth

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.UserTable
import com.fjr619.jwtpostgresql.presentation.plugin.ParsingException
import com.fjr619.jwtpostgresql.presentation.plugin.ValidationException
import com.fjr619.jwtpostgresql.domain.model.params.CreateUserParams
import com.fjr619.jwtpostgresql.domain.service.security.hash.HashingService
import com.fjr619.jwtpostgresql.domain.service.security.hash.SaltedHash
import com.fjr619.jwtpostgresql.data.db.UserEntity
import com.fjr619.jwtpostgresql.domain.repository.auth.AuthRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.koin.core.annotation.Singleton

@Singleton
class AuthRepositoryImpl constructor(
    private val hashingService: HashingService,
    private val databaseFactory: DatabaseFactory
): AuthRepository {
    override suspend fun registerUser(params: CreateUserParams): UserEntity {
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

        return rowToUserEntity(statement?.resultedValues?.get(0))
            ?: throw ParsingException("Error cant register")
    }

    override suspend fun loginUser(email: String, password: String): UserEntity {
        val user = findUserByEmail(email) ?: throw ValidationException("Incorrect username")

        val isValidPassword = hashingService.verify(
            value = password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) throw ValidationException("Incorrect password")
        return user
    }

    override suspend fun findUserByEmail(email: String): UserEntity? {
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

    private fun rowToUserEntity(row: ResultRow?): UserEntity? {
        return if (row == null) null
        else UserEntity(
            id = row[UserTable.id],
            fullName = row[UserTable.fullName],
            avatar = row[UserTable.avatar],
            email = row[UserTable.email],
            salt = row[UserTable.salt],
            password = row[UserTable.password],
            createdAt = row[UserTable.createdAt].toString()
        )
    }
}