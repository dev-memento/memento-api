package com.official.memento.todo.controller.dto;

import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.entity.enums.ToDoType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ToDo 목록 응답")
public record ToDoGetResponse(
        @Schema(description = "ToDo ID")
        long id,

        @Schema(description = "그룹 ID")
        String groupId,

        @Schema(description = "설명")
        String description,

        @Schema(description = "시작일")
        String startDate,

        @Schema(description = "마감일")
        String endDate,

        @Schema(description = "완료 여부")
        boolean isCompleted,

       @Schema(description = "우선순위 값")
        double priorityValue,

        @Schema(description = "우선순위 타입")
        String priorityType,

        @Schema(description = "태그 이름")
        String tagName,

        @Schema(description = "태그 색상")
        String tagColor,

        @Schema(description = "ToDo 유형")
        ToDoType toDoType,

        @Schema(description = "정렬 순서")
        double orderNum
) {
    public static ToDoGetResponse of(ToDo toDo) {
        return new ToDoGetResponse(
                toDo.getId(),
                toDo.getGroupId(),
                toDo.getDescription(),
                toDo.getStartDate().toString(),
                toDo.getEndDate().toString(),
                toDo.isCompleted(),
                toDo.getPriorityValue(),
                toDo.getPriorityType().name(),
                toDo.getTagName() == null ? "" : toDo.getTagName(),
                toDo.getTagColor() == null ? "" : toDo.getTagColor().getHexCode(),
                toDo.getType(),
                // FIXME: NPE나는 상황 찾아서, 수정
                toDo.getOrderNum() == null ? 0.0 : toDo.getOrderNum()
        );
    }
}
