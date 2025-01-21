package com.official.memento.todo.service.command;

import com.official.memento.global.entity.enums.RepeatOption;

import java.time.LocalDate;

public record ToDoCreateCommand(
        long memberId,
        LocalDate startDate,
        String description,
        LocalDate endDate,
        RepeatOption repeatOption,
        LocalDate repeatExpiredDate,
        Long tagId,
        Double priorityUrgency,
        Double priorityImportance
) {
    public static ToDoCreateCommand of(
            final long memberId,
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final RepeatOption repeatOption,
            final LocalDate repeatExpiredDate,
            final Long tagId,
            final Double priorityUrgency,
            final Double priorityImportance
    ) {
        return new ToDoCreateCommand(
                memberId,
                startDate,
                description,
                endDate,
                repeatOption,
                repeatExpiredDate,
                tagId,
                priorityUrgency,
                priorityImportance
        );
    }
}
