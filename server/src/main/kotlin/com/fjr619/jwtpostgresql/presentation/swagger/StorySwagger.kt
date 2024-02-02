package com.fjr619.jwtpostgresql.presentation.swagger

import com.fjr619.jwtpostgresql.base.ServiceResultSerializer
import com.fjr619.jwtpostgresql.domain.model.PaginatedResult
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryUpdateDto
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode

private const val TAG = "STORY"
fun OpenApiRoute.swaggerGetById() {
    tags = listOf(TAG)
    description = "Get story by id"
    securitySchemeNames = setOf("JWT-Auth")

    request {
        pathParameter<Long>("di") {
            description = "Story id"
            example = 1
        }
    }

    response {
        HttpStatusCode.OK to {
            body<ServiceResultSerializer.ServiceResultSurrogate<StoryDto>>()
        }

        failure()
    }
}

fun OpenApiRoute.swaggerCreateStory() {
    tags = listOf(TAG)
    description = "Create new story"
    securitySchemeNames = setOf("JWT-Auth")

    request {
        body<StoryCreatedDto>{
            example("CREATE", StoryCreatedDto(
                title = "Ini story title",
                content = "ini story content",
                isDraft = false
            ))
        }
    }

    response {
        HttpStatusCode.Created to {
            body<ServiceResultSerializer.ServiceResultSurrogate<StoryDto>>()
        }

        failure()
    }
}

fun OpenApiRoute.swaggerUpdateStory() {
    tags = listOf(TAG)
    description = "Update existing story by id"
    securitySchemeNames = setOf("JWT-Auth")

    request {
        pathParameter<Long>("di") {
            description = "Story id"
            example = 1
        }

        body<StoryUpdateDto> {
            example("UPDATE", StoryUpdateDto(
                title = "Ini story title 1",
                content = "ini story kontent 1",
                isDraft = false
            ))
        }
    }

    response {
        HttpStatusCode.OK to {
            body<ServiceResultSerializer.ServiceResultSurrogate<StoryDto>>()
        }

        failure()
    }
}

fun OpenApiRoute.swaggerDeleteStory() {
    tags = listOf(TAG)
    description = "Delete existing story by id"
    securitySchemeNames = setOf("JWT-Auth")

    request {
        pathParameter<Long>("di") {
            description = "Story id"
            example = 1
        }
    }

    response {
        HttpStatusCode.OK to {
            body<ServiceResultSerializer.ServiceResultSurrogate<Boolean>>()
        }

        failure()
    }
}

fun OpenApiRoute.swaggerGetStory() {
    tags = listOf(TAG)
    description = "Get all existing stories by id"
    securitySchemeNames = setOf("JWT-Auth")

    request {
        queryParameter<Int>("page") {
            example = 1
        }

        queryParameter<Int>("limit") {
            example = 10
        }
    }

    response {
        HttpStatusCode.OK to {
            body<ServiceResultSerializer.ServiceResultSurrogate<PaginatedResult<StoryDto>>>()
        }

        failure()
    }
}