package com.official.memento.todo.service.command

data class BrainDumpCreateCommand(
    val memberId: Long,
    val content: String,
)
