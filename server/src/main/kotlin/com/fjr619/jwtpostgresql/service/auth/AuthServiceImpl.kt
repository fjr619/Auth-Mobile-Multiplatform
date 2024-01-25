package com.fjr619.jwtpostgresql.service.auth

import com.fjr619.jwtpostgresql.db.DatabaseFactory
import com.fjr619.jwtpostgresql.db.UserTable
import com.fjr619.jwtpostgresql.models.User
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement

class AuthServiceImpl: AuthService {
    override suspend fun registerUser(params: CreateUserParams): User? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = UserTable.insert {
                it[email] = params.email
                it[password] = params.password //@TODO, we need to encrypt the password
                it[fullName] = params.fullName
                it[avatar] = params.avatar
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUserByEmail(email: String): User? {
        val user = DatabaseFactory.dbQuery {
            UserTable.selectAll().where { UserTable.email.eq(email) }.map {
                rowToUser(it)
            }.singleOrNull()
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
            createdAt = row[UserTable.createdAt].toString()
        )
    }
}