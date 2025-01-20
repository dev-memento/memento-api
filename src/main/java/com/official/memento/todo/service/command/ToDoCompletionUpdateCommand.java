package com.official.memento.todo.service.command;

import java.time.LocalDate;

public record ToDoCompletionUpdateCommand(
        long memberId,
        long toDoId
) {
    public static ToDoCompletionUpdateCommand of(
            final long memberId,
            final long toDoId
    ) {
        return new ToDoCompletionUpdateCommand(
                memberId,
                toDoId
        );
    }
}
