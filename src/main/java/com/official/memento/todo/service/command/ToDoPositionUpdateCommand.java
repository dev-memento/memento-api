package com.official.memento.todo.service.command;

public record ToDoPositionUpdateCommand(
        Long memberId,
        Long toDoId,
        Double targetOrderNum
) {
    public static ToDoPositionUpdateCommand of(
            final Long memberId,
            final Long toDoId,
            final Double targetOrderNum
    ) {
        return new ToDoPositionUpdateCommand(
                memberId,
                toDoId,
                targetOrderNum
        );
    }
}
