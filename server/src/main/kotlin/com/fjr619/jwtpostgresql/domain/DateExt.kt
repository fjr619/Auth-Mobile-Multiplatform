package com.fjr619.jwtpostgresql.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.convertTimeZone(zone: TimeZone): LocalDateTime {
    return this.toInstant(TimeZone.UTC).toLocalDateTime(zone)
}

fun getNowUTC(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
}