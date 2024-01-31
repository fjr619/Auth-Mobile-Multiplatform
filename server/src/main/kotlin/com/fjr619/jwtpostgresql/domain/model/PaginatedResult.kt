package com.fjr619.jwtpostgresql.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResult<T>(
    val pageCount: Long,
    val nextPage: Long?,
    val data: List<T>
)