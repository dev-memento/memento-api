package com.official.memento.member.domain;

import com.official.memento.member.domain.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class MemberPersonalInfo {

    private Long id;
    private long memberId;
    private LocalTime wakeUpTime;
    private LocalTime windDownTime;
    private int timeZoneOffset;
    private JobType job;
    private String jobOtherDetail;
    private Boolean isStressedUnorganizedSchedule;
    private Boolean isForgetImportantThings;
    private Boolean isPreferReminder;
    private Boolean isImportantBreaks;


    public String toPersonalInfoString() {
        return "MemberPersonalInfo" +
                ", wakeUpTime=" + wakeUpTime +
                ", windDownTime=" + windDownTime +
                ", timeZoneOffset=" + timeZoneOffset +
                ", job=" + job +
                ", stressed unorganized schedule isStressedUnorganizedSchedule=" + isStressedUnorganizedSchedule +
                ", often forget important things =" + isForgetImportantThings +
                ", prefer reminders =" + isPreferReminder +
                ", break is important =" + isImportantBreaks +
                '}';
    }

    public static MemberPersonalInfo of(
            final Long memberId,
            final int timeZoneOffset
    ) {
        return new MemberPersonalInfo(
                null,
                memberId,
                null,
                null,
                timeZoneOffset,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public static MemberPersonalInfo withId(
            final Long id,
            final Long memberId,
            final LocalTime wakeUpTime,
            final LocalTime windDownTime,
            final int timeZoneOffset,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks) {
        return new MemberPersonalInfo(
                id,
                memberId,
                wakeUpTime,
                windDownTime,
                timeZoneOffset,
                job,
                jobOtherDetail,
                isStressedUnorganizedSchedule,
                isForgetImportantThings,
                isPreferReminder,
                isImportantBreaks);
    }

    public void update(
            final LocalTime wakeUpTime,
            final LocalTime windDownTime,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks) {
        this.wakeUpTime = wakeUpTime;
        this.windDownTime = windDownTime;
        this.job = job;
        this.jobOtherDetail = jobOtherDetail;
        this.isStressedUnorganizedSchedule = isStressedUnorganizedSchedule;
        this.isForgetImportantThings = isForgetImportantThings;
        this.isPreferReminder = isPreferReminder;
        this.isImportantBreaks = isImportantBreaks;
    }

    public MemberPersonalInfo updateTimeZoneOffset(int timeZoneOffset) {
        return MemberPersonalInfo.withId(
                this.id,
                this.memberId,
                this.wakeUpTime,
                this.windDownTime,
                timeZoneOffset,
                this.job,
                this.jobOtherDetail,
                this.isStressedUnorganizedSchedule,
                this.isForgetImportantThings,
                this.isPreferReminder,
                this.isImportantBreaks);
    }
}