package com.official.memento.todo.controller.dto.response;

import com.official.memento.todo.domain.entity.enums.ToDoType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ToDo 목록 응답")
public record ToDoPrioritizedGetResponse(
        @Schema(description = "ToDo ID")
        Long id,
        @Schema(description = "그룹 ID")
        String groupId,
        @Schema(description = "설명")
        String description,
        @Schema(description = "시작일")
        String startDate,
        @Schema(description = "마감일")
        String endDate,
        @Schema(description = "완료 여부")
        Boolean isCompleted,
        @Schema(description = "우선순위 값")
        Double priorityValue,
        @Schema(description = "우선순위 타입")
        String priorityType,
        @Schema(description = "태그 이름")
        String tagName,
        @Schema(description = "태그 색상")
        String tagColor,
        @Schema(description = "ToDo 유형")
        ToDoType toDoType,
        @Schema(description = "정렬 순서")
        Double orderNum
) {
}
