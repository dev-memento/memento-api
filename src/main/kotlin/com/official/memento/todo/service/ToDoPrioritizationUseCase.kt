package com.official.memento.todo.service

import com.official.memento.todo.domain.ToDo
import com.official.memento.todo.service.command.ToDoPrioritizationCommand

@FunctionalInterface
interface ToDoPrioritizationUseCase {
    fun prioritizeWeekly (command: ToDoPrioritizationCommand): List<List<ToDo>>
    fun prioritizeDaily (command: ToDoPrioritizationCommand): List<ToDo>
}
