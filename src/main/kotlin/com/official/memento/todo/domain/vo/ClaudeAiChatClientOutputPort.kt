package com.official.memento.todo.domain.vo

import com.official.memento.todo.domain.entity.ToDo

interface ClaudeAiChatClientOutputPort {
    fun prioritizeTodo(
            todoList: List<ToDo>,
            orderList: List<Double>,
            personalInfo: String
    ): List<PrioritizedToDo>
}
