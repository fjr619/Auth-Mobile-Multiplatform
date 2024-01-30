package com.fjr619.jwtpostgresql.domain.repository.auth

import com.fjr619.jwtpostgresql.domain.model.User

interface AuthRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: Long): User?
    suspend fun save(entity: User): User?
    suspend fun checkUserNameAndPassword(email: String, password: String): User?
}