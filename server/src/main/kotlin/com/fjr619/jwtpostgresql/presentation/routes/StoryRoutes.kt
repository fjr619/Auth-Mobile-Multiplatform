package com.fjr619.jwtpostgresql.presentation.routes

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.mapper.toDto
import com.fjr619.jwtpostgresql.domain.service.story.StoryService
import com.fjr619.jwtpostgresql.presentation.error.handleRequestError
import com.github.michaelbull.result.mapBoth
import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

private const val ENDPOINT = "api/stories" // Endpoint

fun Application.storyRoutes() {

    val storyService: StoryService by inject()

    routing {
        route(ENDPOINT) {
            authenticate {
                get("{id}") {
                    val storyId = call.parameters["id"]?.toLongOrNull() ?: -1
                    storyService.findById(storyId).mapBoth(
                        success = {
                            call.respond(HttpStatusCode.OK, BaseResponse.SuccessResponse(
                                statusCode = HttpStatusCode.OK,
                                data = it.toDto()
                            ))
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
                }
            }
        }
    }
}