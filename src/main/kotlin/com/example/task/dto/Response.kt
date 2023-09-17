package com.example.task.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Basic rest response object")
open class Response {
    @field:Schema(
        description = "Simple message from the API",
        example = "The prime number search started",
        type = "string"
    )
    var message: String = ""
}