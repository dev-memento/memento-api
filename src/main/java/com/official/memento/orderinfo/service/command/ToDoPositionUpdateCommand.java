package com.official.memento.orderinfo.service.command;

public record ToDoPositionUpdateCommand(
        long memberId,
        long toDoId,
        Long previousToDoId,
        Long nextToDoId
) {
    public static ToDoPositionUpdateCommand of(
            final long memberId,
            final long toDoId,
            final Long previousToDoId,
            final Long nextToDoId
    ) {
        return new ToDoPositionUpdateCommand(
                memberId,
                toDoId,
                previousToDoId,
                nextToDoId
        );
    }
}
