package com.official.memento.todo.service.command;

import com.official.memento.global.entity.enums.RepeatOption;

import java.time.LocalDate;

public record ToDoUpdateCommand(
        long memberId,
        long toDoId,
        LocalDate startDate,
        String description,
        LocalDate endDate,
        Long tagId,
        Double priorityUrgency,
        Double priorityImportance
) {
    public static ToDoUpdateCommand of(
            final long memberId,
            final long toDoId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Long tagId,
            final Double priorityUrgency,
            final Double priorityImportance
    ) {
        return new ToDoUpdateCommand(
                memberId,
                toDoId,
                startDate,
                description,
                endDate,
                tagId,
                priorityUrgency,
                priorityImportance
        );
    }
}
