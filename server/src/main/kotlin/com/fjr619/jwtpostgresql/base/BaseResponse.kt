package com.fjr619.jwtpostgresql.base

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.ktor.http.HttpStatusCode
import java.lang.Exception


sealed class BaseResponse<T>(
    open val statusCode: HttpStatusCode
) {
//    @JsonSerialize
    data class SuccessResponse<T>(
        val data: T?,
        val message: String? = null,
        override val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(statusCode)

    @JsonSerialize
    data class ErrorResponse<T>(
        val message: String? = null,
        override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : BaseResponse<T>(statusCode)
}