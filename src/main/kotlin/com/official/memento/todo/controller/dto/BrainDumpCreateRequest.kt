package com.official.memento.todo.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class BrainDumpCreateRequest
    @JsonCreator
    constructor(
        @JsonProperty("content") val content: String,
    )
