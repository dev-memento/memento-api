package com.official.memento.schedule.service.command;

import java.util.List;

public record SchedulesCreateCommand(
        List<ScheduleCreateCommand> commands
) {

}
