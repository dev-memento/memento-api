package com.official.memento.todo.controller.dto;

import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.domain.enums.ToDoType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "단일 ToDo 디테일 응답")
public record ToDoDetailGetResponse(
        @Schema(description = "ToDo ID")
        long id,

        @Schema(description = "설명")
        String description,

        @Schema(description = "시작일")
        String startDate,

        @Schema(description = "마감일")
        String endDate,

        @Schema(description = "완료 여부")
        boolean isCompleted,

        @Schema(description = "우선순위 타입")
        String priorityType,

        @Schema(description = "태그 ID")
        Long tagId,

        @Schema(description = "태그 이름")
        String tagName,

        @Schema(description = "태그 색상")
        String tagColor,

        @Schema(description = "ToDo 유형")
        ToDoType toDoType
) {
    public static ToDoDetailGetResponse of(ToDo toDo) {
        return new ToDoDetailGetResponse(
                toDo.getId(),
                toDo.getDescription(),
                toDo.getStartDate().toString(),
                toDo.getEndDate().toString(),
                toDo.isCompleted(),
                toDo.getPriorityType().name(),
                toDo.getTagId() == null ? 0L : toDo.getTagId(),
                toDo.getTagName() == null ? "" : toDo.getTagName(),
                toDo.getTagColor() == null ? "" : toDo.getTagColor().getHexCode(),
                toDo.getType()
        );
    }
}
