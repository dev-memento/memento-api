package com.official.memento.todo.service.command;

import com.official.memento.global.entity.enums.RepeatOption;

import java.time.LocalDate;

public record ToDoUpdateCommand(
        long memberId,
        long toDoId,
        LocalDate date,
        String description,
        LocalDate deadline,
        Long tagId,
        Double priorityUrgency,
        Double priorityImportance
) {
    public static ToDoUpdateCommand of(
            final long memberId,
            final long toDoId,
            final LocalDate date,
            final String description,
            final LocalDate deadline,
            final Long tagId,
            final Double priorityUrgency,
            final Double priorityImportance
    ) {
        return new ToDoUpdateCommand(
                memberId,
                toDoId,
                date,
                description,
                deadline,
                tagId,
                priorityUrgency,
                priorityImportance
        );
    }
}
