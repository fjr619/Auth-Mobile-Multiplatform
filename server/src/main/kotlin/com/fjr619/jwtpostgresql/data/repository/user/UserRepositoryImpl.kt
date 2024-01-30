package com.fjr619.jwtpostgresql.data.repository.user

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.db.UserTable
import com.fjr619.jwtpostgresql.domain.model.mapper.toModel
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.repository.user.UserRepository
import com.fjr619.jwtpostgresql.domain.service.security.hash.HashingService
import com.fjr619.jwtpostgresql.domain.service.security.hash.SaltedHash
import mu.KLogger
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
class UserRepositoryImpl constructor(
    private val hashingService: HashingService,
    private val databaseFactory: DatabaseFactory,
    private val logger: KLogger
) : UserRepository {



//    /**
//     * Login User
//     * @param email Email
//     * @param password Password
//     * @return User
//     */
//    override suspend fun loginUser(email: String, password: String): User {
//        val user = findUserByEmail(email) ?: throw ValidationException("Incorrect username")
//
//        //example
//        logger.debug {
//            "user time = ${user.createdAt.convertTimeZone(TimeZone.currentSystemDefault())}"
//        }
//
//        val isValidPassword = hashingService.verify(
//            value = password,
//            saltedHash = SaltedHash(
//                hash = user.password,
//                salt = user.salt
//            )
//        )
//
//        if (!isValidPassword) throw ValidationException("Incorrect password")
//        return user.toModel()
//    }



    override suspend fun findByEmail(email: String): User? {
        return databaseFactory.dbQuery {
            UserTable.selectAll().where { UserTable.email.eq(email) }.map {
                it.toModel()
            }.singleOrNull()
        }
    }

    override suspend fun findById(id: Long): User? {
        return databaseFactory.dbQuery {
            UserTable.selectAll().where { UserTable.id.eq(id) }.map {
                it.toModel()
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

        return statement?.resultedValues?.get(0).toModel()
    }

    private fun update(entity: User): User? {
        return null
    }
}