package com.fjr619.jwtpostgresql.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResult<T>(
    val dataCount: Long,
    val pageCount: Long,
    val nextPage: Long?,
    val list: List<T>
)