package com.official.memento.member.service.command;

import java.time.LocalTime;

public record MemberWindDownUpdateCommand(
        long memberId,
        LocalTime windDownTime
) {
    public static MemberWindDownUpdateCommand of(
            final long memberId,
            final String windDownTime
    ){
        return new MemberWindDownUpdateCommand(
                memberId,
                LocalTime.parse(windDownTime)
        );
    }
}
