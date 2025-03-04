package com.official.memento.member.service.command;

public record MemberPersonalInfoCreateCommand(
        long memberId
) {
    public static MemberPersonalInfoCreateCommand from(final Long memberId) {
        return new MemberPersonalInfoCreateCommand(memberId);
    }
}
