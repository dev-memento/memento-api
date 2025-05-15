package com.official.memento.member.service.command;

import java.time.ZoneOffset;

public record MemberPersonalInfoCreateCommand(
        long memberId,
        String timeZoneOffset
) {
    public static MemberPersonalInfoCreateCommand from(final Long memberId, final String timeZoneOffset) {
        return new MemberPersonalInfoCreateCommand(memberId, timeZoneOffset);
    }
}
