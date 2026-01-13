package com.official.memento.orderinfo.service.command;

import java.time.LocalDate;

public record ToDoPositionUpdateCommand(
        long memberId,
        long toDoId,
        LocalDate date,
        Long previousToDoId,
        Long nextToDoId
) {
    public static ToDoPositionUpdateCommand of(
            final long memberId,
            final long toDoId,
            final LocalDate date,
            final Long previousToDoId,
            final Long nextToDoId
    ) {
        return new ToDoPositionUpdateCommand(
                memberId,
                toDoId,
                date,
                previousToDoId,
                nextToDoId
        );
    }
}
