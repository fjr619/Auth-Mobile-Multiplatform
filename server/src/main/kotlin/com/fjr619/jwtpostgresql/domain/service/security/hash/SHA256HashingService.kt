package com.fjr619.jwtpostgresql.domain.service.security.hash

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.koin.core.annotation.Singleton
import java.security.SecureRandom

//https://www.youtube.com/watch?v=uezSuUQt6DY&t=8s
@Singleton
class SHA256HashingService: HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}