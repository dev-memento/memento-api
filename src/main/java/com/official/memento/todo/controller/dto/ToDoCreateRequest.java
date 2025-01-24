package com.official.memento.todo.controller.dto;

import com.official.memento.global.exception.InvalidRequestBodyException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import static com.official.memento.global.exception.ErrorCode.NULL_DATA_ERROR;

@Schema(name = "ToDo 생성 요청")
public record ToDoCreateRequest(
        @Schema(description = "ToDo 날짜 (Today or 사용자 지정)", example = "2025-01-20")
        LocalDate startDate,
        @Schema(description = "ToDo 내용", maxLength = 30, example = "팀 프로젝트 준비")
        String description,
        @Schema(description = "마감 기한", example = "2025-01-25")
        LocalDate endDate,
        @Schema(description = "태그 ID", example = "12")
        long tagId,
        @Schema(description = "긴급도 우선순위 (0~1)", example = "0.5")
        Double priorityUrgency,
        @Schema(description = "중요도 우선순위 (0~1)", example = "0.5")
        Double priorityImportance
) {
    public ToDoCreateRequest(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final long tagId,
            final Double priorityUrgency,
            final Double priorityImportance
    ) {
        checkNullData(startDate, description);
        this.startDate = startDate;
        this.description = description;
        this.endDate = endDate;
        this.tagId = tagId;
        this.priorityUrgency = priorityUrgency;
        this.priorityImportance = priorityImportance;
    }

    private static void checkNullData(
            final LocalDate startDate,
            final String description
    ) {
        if (startDate == null || description == null) {
            throw new InvalidRequestBodyException(NULL_DATA_ERROR);
        }
    }
}
