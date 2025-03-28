package com.official.memento.tag.service.command;

public record TagDeleteCommand(
        long memberId,
        long tagId
) {
    public static TagDeleteCommand of(final long memberId, final long tagId){
        return new TagDeleteCommand(memberId, tagId);
    }
}
