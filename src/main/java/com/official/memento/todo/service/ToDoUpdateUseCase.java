package com.official.memento.todo.service;

import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;

public interface ToDoUpdateUseCase {
    void update(final ToDoUpdateCommand toDoUpdateCommand);
    boolean updateCompletion(final ToDoCompletionUpdateCommand toDoCompletionUpdateCommand);
    void updatePosition(final ToDoPositionUpdateCommand toDoPositionUpdateCommand);
}
