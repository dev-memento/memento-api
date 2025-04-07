package com.official.memento.member.service.command;

public record MemberDeleteCommand(
        long memberId
) {
    public static MemberDeleteCommand of(final long memberId) {
        return new MemberDeleteCommand(memberId);
    }
}
