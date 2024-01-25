package com.fjr619.jwtpostgresql.models

data class User(
    val id: Int,
    val fullName: String,
    val avatar: String,
    val email: String,
    val password: String,
    var authToken: String? = null,
    var salt: String,
    val createdAt: String
)
