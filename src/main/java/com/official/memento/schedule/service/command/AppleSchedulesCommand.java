package com.official.memento.schedule.service.command;

import java.util.List;

public record AppleSchedulesCommand(
        long memberId,
        String syncToken,
        List<AppleScheduleCreateCommand> commands
) {
    public static AppleSchedulesCommand of(
            final long memberId,
            final String syncToken,
            List<AppleScheduleCreateCommand> commands
    ) {
        return new AppleSchedulesCommand(memberId, syncToken, commands);
    }
}
