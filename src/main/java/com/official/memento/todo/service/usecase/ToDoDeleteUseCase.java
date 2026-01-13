package com.official.memento.todo.service.usecase;

import com.official.memento.todo.service.command.ToDoDeleteCommand;

public interface ToDoDeleteUseCase {
    void delete(final ToDoDeleteCommand toDoDeleteCommand);
    void deleteAllByMemberId(final long memberId);
}
