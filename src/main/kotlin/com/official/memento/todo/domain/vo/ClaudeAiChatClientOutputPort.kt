package com.official.memento.todo.domain.vo

import com.official.memento.todo.domain.ToDo

interface ClaudeAiChatClientOutputPort {
    suspend fun prioritizeTodo(
        todoList: List<ToDo>,
        orderList: List<Int>,
    ): List<PrioritizedToDo>
}
