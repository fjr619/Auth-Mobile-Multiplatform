package com.fjr619.jwtpostgresql.domain.repository

import com.fjr619.jwtpostgresql.domain.model.User

interface UserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: Long): User?
    suspend fun save(entity: User): User?
    suspend fun checkUserNameAndPassword(email: String, password: String): User?
}