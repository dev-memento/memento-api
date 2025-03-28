package com.official.memento.member.service.command;

public record MemberTimeZoneUpdateCommand(
        long memberId,
        int timeZoneOffset
) {
    public static MemberTimeZoneUpdateCommand of(
            final long memberId,
            final int timeZoneOffset
    ) {
        return new MemberTimeZoneUpdateCommand(
                memberId,
                timeZoneOffset
        );
    }
}
