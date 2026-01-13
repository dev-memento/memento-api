package com.official.memento.tag.service.result;

import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;

public record TagResult(
    Long id,
    String name,
    TagColor color,
    Long memberId
) {
    public static TagResult of(final Tag tag) {
        return new TagResult(
            tag.getId(),
            tag.getName(),
            tag.getColor(),
            tag.getMemberId()
        );
    }
}
