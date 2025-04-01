package com.official.memento.member.service.command;

import java.time.LocalTime;

public record MemberUpTimeUpdateCommand(
        long memberId,
        LocalTime wakeUpTime
) {
    public static MemberUpTimeUpdateCommand of(
            final long memberId,
            final LocalTime wakeUpTime
    ){
        return new MemberUpTimeUpdateCommand(
                memberId,
                wakeUpTime
        );
    }
}
