package com.official.memento.todo.service.command;

public record ToDoPositionUpdateCommand(
        Long memberId,
        Long toDoId,
        Integer targetOrderNum
) {
    public static ToDoPositionUpdateCommand of(
            final Long memberId,
            final Long toDoId,
            final Integer targetOrderNum
    ) {
        return new ToDoPositionUpdateCommand(
                memberId,
                toDoId,
                targetOrderNum
        );
    }
}
