package com.official.memento.todo.service.command

import java.time.LocalDate

data class ToDoPrioritizationCommand(
    val memberId: Long,
    val targetDate: LocalDate,
)
