package com.official.memento.todo.service.command;

import com.official.memento.global.entity.enums.RepeatOption;

import java.time.LocalDate;

public record ToDoUpdateCommand(
        long memberId,
        long toDoId,
        LocalDate startDate,
        String description,
        LocalDate endDate,
        long tagId,
        Double priorityUrgency,
        Double priorityImportance
) {
    public static ToDoUpdateCommand of(
            final long memberId,
            final long toDoId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final long tagId,
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
