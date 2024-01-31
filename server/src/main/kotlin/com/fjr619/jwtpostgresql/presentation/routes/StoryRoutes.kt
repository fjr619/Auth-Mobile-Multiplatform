package com.fjr619.jwtpostgresql.presentation.routes

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryUpdateDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toDto
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import com.fjr619.jwtpostgresql.domain.service.story.StoryService
import com.fjr619.jwtpostgresql.domain.service.user.UserService
import com.fjr619.jwtpostgresql.presentation.error.getEmail
import com.fjr619.jwtpostgresql.presentation.error.getUserId
import com.fjr619.jwtpostgresql.presentation.error.handleRequestError
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapBoth
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

private const val ENDPOINT = "api/stories" // Endpoint

fun Application.storyRoutes() {

    val storyService: StoryService by inject()
    val userService: UserService by inject()
    val tokenConfig: TokenConfig by inject()
    val tokenService: TokenService by inject()

    routing {
        route(ENDPOINT) {
            authenticate {

                //get story
                get("{id}") {
                    val storyId = call.parameters["id"]?.toLongOrNull() ?: -1
                    storyService.findById(storyId).mapBoth(
                        success = {
                            val token = it.user?.generateToken(tokenConfig, tokenService)
                            call.respond(HttpStatusCode.OK, BaseResponse.SuccessResponse(
                                statusCode = HttpStatusCode.OK,
                                data = it.toDto(),
                                authToken = token
                            ).toResponse())
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
                }

                //add story
                post ("/") {
                    val requestBody = call.receive<StoryCreatedDto>()
                    val token = generateToken(tokenConfig, tokenService, getUserId(), getEmail())

                    userService.findById(getUserId()).andThen {
                        storyService.add(it, requestBody)
                    }.mapBoth(
                        success = {
                            call.respond(HttpStatusCode.Created, BaseResponse.SuccessResponse(
                                statusCode = HttpStatusCode.Created,
                                data = it.toDto(),
                                authToken = token
                            ).toResponse())
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
                }

                //update story
                put ("/") {
                    val requestBody = call.receive<StoryUpdateDto>()
                    val token: String = generateToken(tokenConfig, tokenService, getUserId(), getEmail())

                    userService.findById(getUserId()).andThen {
                        storyService.update(it, requestBody)
                    }.mapBoth(
                        success = {
                            call.respond(HttpStatusCode.OK, BaseResponse.SuccessResponse(
                                statusCode = HttpStatusCode.OK,
                                data = it.toDto(),
                                authToken = token
                            ).toResponse())
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