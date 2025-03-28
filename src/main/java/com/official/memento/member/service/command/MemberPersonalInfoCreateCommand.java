package com.official.memento.member.service.command;

public record MemberPersonalInfoCreateCommand(
        long memberId,
        int timeZoneOffset
) {
    public static MemberPersonalInfoCreateCommand from(final Long memberId, final int timeZoneOffset) {
        return new MemberPersonalInfoCreateCommand(memberId, timeZoneOffset);
    }
}
