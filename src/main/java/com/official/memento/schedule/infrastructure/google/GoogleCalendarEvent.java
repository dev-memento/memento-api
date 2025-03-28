package com.official.memento.schedule.infrastructure.google;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public record GoogleCalendarEvent(
        String id,
        String status,
        String summary,
        StartEnd start,
        StartEnd end,
        List<String> recurrence,
        String recurringEventId
) {
    public Schedule toSchedule(final long memberId, final long tagId) {
        RepeatOption repeatOption = RepeatOption.NONE;
        LocalDate repeatEndDate = null;
        if (this.recurrence != null) {
            for (String rule : this.recurrence) {
                if (rule.contains("FREQ=")) {
                    String freq = rule.substring(rule.indexOf("FREQ=") + 5, rule.indexOf(";"));
                    repeatOption = RepeatOption.valueOf(freq.toUpperCase(Locale.ROOT));
                }
                if (rule.contains("UNTIL=")) {
                    String until = rule.substring(rule.indexOf("UNTIL=") + 6, rule.indexOf("T"));
                    repeatEndDate = LocalDate.parse(until);
                }
            }
        }

        return Schedule.of(
                memberId,
                this.summary() != null ? this.summary() : "", // description
                this.start().toLocalDateTime(),
                this.end().toLocalDateTime(),
                this.start().date() != null,
                repeatOption,
                repeatEndDate,
                ScheduleType.GOOGLE,
                this.id(),
                tagId
        );
    }
}
