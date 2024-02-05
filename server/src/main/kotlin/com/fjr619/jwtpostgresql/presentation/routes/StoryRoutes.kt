package com.fjr619.jwtpostgresql.presentation.routes

import Response
import com.fjr619.jwtpostgresql.domain.model.PaginatedResult
import com.fjr619.jwtpostgresql.domain.model.RequestError
import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import com.fjr619.jwtpostgresql.domain.model.dto.StoryUpdateDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toDto
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import com.fjr619.jwtpostgresql.domain.service.security.token.generateToken
import com.fjr619.jwtpostgresql.domain.service.security.token.getEmail
import com.fjr619.jwtpostgresql.domain.service.security.token.getUserId
import com.fjr619.jwtpostgresql.domain.service.story.StoryService
import com.fjr619.jwtpostgresql.domain.service.user.UserService
import com.fjr619.jwtpostgresql.presentation.error.handleRequestError
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerCreateStory
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerDeleteStory
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerGetById
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerGetStory
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerUpdateStory
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapBoth
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

private const val ENDPOINT = "api/story" // Endpoint

fun Application.storyRoutes() {

    val storyService: StoryService by inject()
    val userService: UserService by inject()
    val tokenConfig: TokenConfig by inject()
    val tokenService: TokenService by inject()

    routing {
        route(ENDPOINT) {
            authenticate {

                //get all my stories api/stories/list?page=1&limit=3
                get("list", { swaggerGetStory() }) {
                    val userId = getUserId()
                    val token = generateToken(tokenConfig, tokenService, userId, getEmail())

                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

                    storyService.getList(userId, page, limit).andThen {
                        val result = it
                        val data = result.list.map {
                            it.toDto()
                        }
                        Ok(PaginatedResult(it.dataCount, it.pageCount, it.nextPage, data))
                    }.mapBoth(
                        success = {
                            call.respond(
                                HttpStatusCode.OK,
                                Response.SuccessResponse(
                                    statusCode = HttpStatusCode.OK.value,
                                    data = it,
                                    token = token
                                ).toResponse()
                            )
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
                }

                //get story
                get("{id}", { swaggerGetById() }) {
                    val storyId = call.parameters["di"]?.toLongOrNull() ?: -1
                    lateinit var token: String

                    storyService.findById(storyId)
                        .andThen(
                            transform = { story ->
                                story.user?.let { user ->
                                    token =
                                        generateToken(
                                            tokenConfig,
                                            tokenService,
                                            user.id,
                                            user.email
                                        )
                                    Ok(story)
                                } ?: Err(RequestError.NotFound("User Not Found"))
                            }
                        )
                        .mapBoth(
                            success = {
                                call.respond(
                                    HttpStatusCode.OK, Response.SuccessResponse(
                                        statusCode = HttpStatusCode.OK.value,
                                        data = it.toDto(),
                                        token = token
                                    ).toResponse()
                                )
                            },
                            failure = {
                                handleRequestError(it)

                            }
                        )
                }

                //add story
                post("", { swaggerCreateStory() }) {
                    val requestBody = call.receive<StoryCreatedDto>()
                    val token = generateToken(tokenConfig, tokenService, getUserId(), getEmail())

                    userService.findById(getUserId()).andThen {
                        storyService.add(it, requestBody)
                    }.mapBoth(
                        success = {
                            call.respond(
                                HttpStatusCode.Created, Response.SuccessResponse(
                                    statusCode = HttpStatusCode.Created.value,
                                    data = it.toDto(),
                                    token = token
                                ).toResponse()
                            )
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
                }

                //update story
                put("{id}", { swaggerUpdateStory() }) {
                    call.parameters["di"]?.toLong()?.let { id ->
                        val requestBody = call.receive<StoryUpdateDto>()
                        val token: String =
                            generateToken(tokenConfig, tokenService, getUserId(), getEmail())
                        userService.findById(getUserId()).andThen { user ->
                            storyService.update(user, id, requestBody)
                        }.mapBoth(
                            success = {
                                call.respond(
                                    HttpStatusCode.OK, Response.SuccessResponse(
                                        statusCode = HttpStatusCode.OK.value,
                                        data = it.toDto(),
                                        token = token
                                    ).toResponse()
                                )
                            },
                            failure = {
                                handleRequestError(it)
                            }
                        )
                    }
                }

                //delete story
                delete("{id}", { swaggerDeleteStory() }) {
                    val storyId = call.parameters["di"]?.toLongOrNull() ?: -1
                    val userId = getUserId()
                    val token: String = generateToken(tokenConfig, tokenService, userId, getEmail())

                    storyService.delete(userId, storyId).mapBoth(
                        success = {
                            call.respond(
                                HttpStatusCode.OK, Response.SuccessResponse(
                                    statusCode = HttpStatusCode.OK.value,
                                    data = it,
                                    token = token
                                ).toResponse()
                            )
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