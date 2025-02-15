package com.official.memento.todo.controller.dto;

import com.official.memento.global.util.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = " ToDo 수정 요청")
public record ToDoUpdateRequest(
        @Schema(description = "수정 ToDo 날짜 (Today or 사용자 지정)", example = "2025-01-20")
        LocalDate startDate,
        @Schema(description = "수정 ToDo 내용", maxLength = 30, example = "팀 프로젝트 준비")
        String description,
        @Schema(description = "수정 마감 기한", example = "2025-01-25")
        LocalDate endDate,
        @Schema(description = "수정 태그 ID", example = "12345")
        long tagId,
        @Schema(description = "긴급도 우선순위 (0~1)", example = "0.5", nullable = true)
        Double priorityUrgency,
        @Schema(description = "중요도 우선순위 (0~1)", example = "0.5", nullable = true)
        Double priorityImportance
) {
    public ToDoUpdateRequest of(
            final LocalDate startDate,
            final String description,
            final LocalDate endDate,
            final Long tagId,
            final Double priorityUrgency,
            final Double priorityImportance
    ) {
        checkNullData(startDate, description);
        return new ToDoUpdateRequest(startDate, description, endDate, tagId, priorityUrgency, priorityImportance);
    }

    private static void checkNullData(final LocalDate startDate, final String description) {
        Validator.isNull(startDate);
        Validator.isNull(description);
        Validator.validateLengthContainEmoji(description, 30);
    }
}
