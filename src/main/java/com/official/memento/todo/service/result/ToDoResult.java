package com.official.memento.todo.service.result;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.entity.enums.ToDoType;

import java.time.LocalDate;

public record ToDoResult(
        Long id,
        Long memberId,
        String groupId,
        LocalDate startDate,
        String description,
        LocalDate endDate,
        boolean isCompleted,
        RepeatOption repeatOption,
        LocalDate repeatExpiredDate,
        Double priorityUrgency,
        Double priorityImportance,
        Double priorityValue,
        PriorityType priorityType,
        ToDoType type,
        Double orderNum,
        Long tagId,
        String tagName,
        TagColor tagColor
) {
    public static ToDoResult of(final ToDo toDo) {
        return new ToDoResult(
                toDo.getId(),
                toDo.getMemberId(),
                toDo.getGroupId(),
                toDo.getStartDate(),
                toDo.getDescription(),
                toDo.getEndDate(),
                toDo.isCompleted(),
                toDo.getRepeatOption(),
                toDo.getRepeatExpiredDate(),
                toDo.getPriorityUrgency(),
                toDo.getPriorityImportance(),
                toDo.getPriorityValue(),
                toDo.getPriorityType(),
                toDo.getType(),
                toDo.getOrderNum(),
                toDo.getTagId(),
                toDo.getTagName(),
                toDo.getTagColor()
        );
    }
}
