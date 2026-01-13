package com.official.memento.todo.controller.dto;

import com.official.memento.todo.domain.entity.enums.ToDoType;
import com.official.memento.todo.service.result.ToDoResult;
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
    public static ToDoDetailGetResponse of(ToDoResult toDoResult) {
        return new ToDoDetailGetResponse(
                toDoResult.id(),
                toDoResult.description(),
                toDoResult.startDate().toString(),
                toDoResult.endDate().toString(),
                toDoResult.isCompleted(),
                toDoResult.priorityType().name(),
                toDoResult.tagId() == null ? 0L : toDoResult.tagId(),
                toDoResult.tagName() == null ? "" : toDoResult.tagName(),
                toDoResult.tagColor() == null ? "" : toDoResult.tagColor().getHexCode(),
                toDoResult.type()
        );
    }
}
