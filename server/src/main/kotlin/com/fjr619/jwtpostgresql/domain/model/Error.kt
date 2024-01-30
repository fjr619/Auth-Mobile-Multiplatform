package com.fjr619.jwtpostgresql.domain.model

sealed class RequestError(val message: String) {
    class NotFound(message: String) : RequestError(message)
    class BadRequest(message: String) : RequestError(message)
    class BadCredentials(message: String) : RequestError(message)
    class BadRole(message: String) : RequestError(message)
    class Unauthorized(message: String) : RequestError(message)
    class Forbidden(message: String) : RequestError(message)
}