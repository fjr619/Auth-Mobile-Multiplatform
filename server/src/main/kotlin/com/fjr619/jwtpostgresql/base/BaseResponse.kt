package com.fjr619.jwtpostgresql.base

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable(with = ServiceResultSerializer::class)
sealed class BaseResponse<out T: Any>(
    open val statusCode: HttpStatusCode
) {
    data class SuccessResponse<out T : Any>(
        val data: T?,
        val message: String? = null,

        @Contextual
        @Serializable(with = HttpStatusCodeSerializer::class)
        override val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(statusCode) {
        fun toResponse() = this as BaseResponse<T>
    }


    data class ErrorResponse(
        val message: String? = null,

        @Contextual
        @Serializable(with = HttpStatusCodeSerializer::class)
        override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : BaseResponse<Nothing>(statusCode) {
        fun toResponse() = this as BaseResponse<Nothing>
    }
}