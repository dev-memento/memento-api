package com.official.memento.todo.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(name = "우선순위 요청")
data class PrioritizationRequest(
    @Schema(description = "정렬하고 싶은 날짜", example = "2021-08-01")
    val targetDate: LocalDate,
)
