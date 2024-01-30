package com.fjr619.jwtpostgresql.domain.validator

import com.fjr619.jwtpostgresql.domain.model.dto.StoryCreatedDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.storyValidation() {
    validate<StoryCreatedDto> { story ->
        if(story.title.isBlank()){
            ValidationResult.Invalid("The title cannot be empty")
        } else if (story.content.isBlank()) {
            ValidationResult.Invalid("The story cannot be empty")
        } else {
            ValidationResult.Valid
        }

    }
}