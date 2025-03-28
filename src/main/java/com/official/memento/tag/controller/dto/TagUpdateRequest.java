package com.official.memento.tag.controller.dto;

import com.official.memento.tag.domain.enums.TagColor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="태그 수정 요청")
public record TagUpdateRequest(
        @Schema(description = "태그 이름")
        String name,
        @Schema(description = "태그 색상")
        TagColor color
) {
}
