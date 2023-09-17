package com.example.task.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Extended rest response object with result specific data")
class ResultResponse : Response() {
    @field:Schema(
        description = "The found prime number list in the given interval",
        type = "list<int>"
    )
    var result: List<Int> = ArrayList()
    @field:Schema(
        description = "Number of found prime number in the given interval",
        example = "100",
        type = "int"
    )
    var resultCount: Int = 0
    @field:Schema(
        description = "Shows if the next page needed when the result is too large",
        example = "true",
        type = "boolean"
    )
    var nextPageNeeded: Boolean = false
    @field:Schema(
        description = "Next page start index",
        example = "true",
        type = "boolean"
    )
    var nextStartIndex: Int = 0
}