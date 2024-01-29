package com.fjr619.jwtpostgresql.base

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.lang.Exception

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
    ) : BaseResponse<T>(statusCode)


    data class ErrorResponse(
        val message: String? = null,

        @Contextual
        @Serializable(with = HttpStatusCodeSerializer::class)
        override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : BaseResponse<Nothing>(statusCode)
}