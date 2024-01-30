package com.fjr619.jwtpostgresql.base

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable(with = ServiceResultSerializer::class)
sealed class BaseResponse<out T: Any>(
    open val statusCode: HttpStatusCode,
    open val authToken: String?
) {
    data class SuccessResponse<out T : Any>(
        val data: T?,
        val message: String? = null,
        override val authToken: String? = null,

        @Contextual
        @Serializable(with = HttpStatusCodeSerializer::class)
        override val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(statusCode, authToken) {
        fun toResponse() = this as BaseResponse<T>
    }


    data class ErrorResponse(
        val message: String? = null,
        override val authToken: String? = null,

        @Contextual
        @Serializable(with = HttpStatusCodeSerializer::class)
        override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : BaseResponse<Nothing>(statusCode, authToken) {
        fun toResponse() = this as BaseResponse<Nothing>
    }
}