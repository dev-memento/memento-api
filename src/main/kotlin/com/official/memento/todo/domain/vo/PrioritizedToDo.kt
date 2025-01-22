package com.official.memento.todo.domain.vo

import java.time.LocalDate

data class PrioritizedToDo(
    val task: String,
    val id: Long,
    val createdDate: LocalDate,
    val deadline: LocalDate,
    val priority: Float,
    val urgency: Float,
    val importance: Float,
    val order: Int,
)
