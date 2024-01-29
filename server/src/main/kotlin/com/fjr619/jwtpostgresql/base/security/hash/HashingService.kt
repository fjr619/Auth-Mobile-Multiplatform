package com.fjr619.jwtpostgresql.base.security.hash

interface HashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 12): SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}