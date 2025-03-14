package com.official.memento.tag.service.command;

import com.official.memento.tag.domain.enums.TagColor;

public record TagUpdateCommand(
        long memberId,
        long tagId,
        TagColor color,
        String name
) {
    public static TagUpdateCommand of(
            long memberId,
            long tagId,
            TagColor color,
            String name
    ) {
        return new TagUpdateCommand(memberId, tagId, color, name);
    }
}
