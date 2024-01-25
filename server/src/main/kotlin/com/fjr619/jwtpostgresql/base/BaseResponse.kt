package com.fjr619.jwtpostgresql.base

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.ktor.http.HttpStatusCode
import java.lang.Exception


sealed class BaseResponse<T>(
    val statusCode: HttpStatusCode = HttpStatusCode.OK
) {
    @JsonSerialize
    data class SuccessResponse<T>(
        val data: T?,
        val message: String? = null,
    ) : BaseResponse<T>()

    @JsonSerialize
    data class ErrorResponse<T>(
        val exception: T? = null,
        val message: String? = null,
    ) : BaseResponse<T>()
}