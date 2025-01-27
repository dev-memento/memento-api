package com.official.memento.todo.domain.vo

import com.official.memento.todo.domain.ToDo

interface ClaudeAiChatClientOutputPort {
    fun prioritizeTodo(
        todoList: List<ToDo>,
        orderList: List<Int>,
        personalInfo: String
    ): List<PrioritizedToDo>
}
