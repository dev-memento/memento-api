package com.official.memento.todo.domain.vo

import java.time.LocalDate

data class ToDoBrainDump(
    val task: String,
    val createdDate: LocalDate,
    val deadline: LocalDate,
    val priority: Float,
    val urgency: Float,
    val importance: Float,
)
