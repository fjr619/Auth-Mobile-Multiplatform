package com.fjr619.jwtpostgresql.service.auth

import com.fjr619.jwtpostgresql.db.DatabaseFactory
import com.fjr619.jwtpostgresql.db.UserTable
import com.fjr619.jwtpostgresql.models.User
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import com.fjr619.jwtpostgresql.security.hash.HashingService
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement

class AuthServiceImpl constructor(
    private val hashingService: HashingService
): AuthService {
    override suspend fun registerUser(params: CreateUserParams): User? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            val saltedHash = hashingService.generateSaltedHash(params.password)
            statement = UserTable.insert {
                it[email] = params.email
                it[password] = saltedHash.hash
                it[fullName] = params.fullName
                it[avatar] = params.avatar
                it[salt] = saltedHash.salt
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUserByEmail(email: String): User? {
        val user = try {
            DatabaseFactory.dbQuery {
                UserTable.selectAll().where { UserTable.email.eq(email) }.map {
                    rowToUser(it)
                }.singleOrNull()
            }
        } catch (e: Exception) {
            throw e
        }

        return user
    }

    private fun rowToUser(row: ResultRow?): User? {
        return if (row == null) null
        else User(
            id = row[UserTable.id],
            fullName = row[UserTable.fullName],
            avatar = row[UserTable.avatar],
            email = row[UserTable.email],
            salt = row[UserTable.salt],
            createdAt = row[UserTable.createdAt].toString()
        )
    }
}