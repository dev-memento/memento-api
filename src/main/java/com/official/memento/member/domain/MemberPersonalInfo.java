package com.official.memento.member.domain;

import com.official.memento.member.domain.enums.JobType;

import java.time.LocalTime;

public class MemberPersonalInfo {

    private Long id;
    private long memberId;
    private LocalTime wakeUpTime;
    private LocalTime windDownTime;
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
                ", job=" + job +
                ", stressed unorganized schedule isStressedUnorganizedSchedule=" + isStressedUnorganizedSchedule +
                ", often forget important things =" + isForgetImportantThings +
                ", prefer reminders =" + isPreferReminder +
                ", break is important =" + isImportantBreaks +
                '}';
    }

    public static MemberPersonalInfo of(
            final Long memberId)
    {
        return new MemberPersonalInfo(
                memberId,
                null,
                null,
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
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks)
    {
        return new MemberPersonalInfo(
                id,
                memberId,
                wakeUpTime,
                windDownTime,
                job,
                jobOtherDetail,
                isStressedUnorganizedSchedule,
                isForgetImportantThings,
                isPreferReminder,
                isImportantBreaks);
    }

    public MemberPersonalInfo(
            final Long memberId,
            final LocalTime wakeUpTime,
            final LocalTime windDownTime,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks)
    {
        this.memberId = memberId;
        this.wakeUpTime = wakeUpTime;
        this.windDownTime = windDownTime;
        this.job = job;
        this.jobOtherDetail = jobOtherDetail;
        this.isStressedUnorganizedSchedule = isStressedUnorganizedSchedule;
        this.isForgetImportantThings = isForgetImportantThings;
        this.isPreferReminder = isPreferReminder;
        this.isImportantBreaks = isImportantBreaks;
    }

    public MemberPersonalInfo(
            final Long id,
            final Long memberId,
            final LocalTime wakeUpTime,
            final LocalTime windDownTime,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks)
    {
        this.id = id;
        this.memberId = memberId;
        this.wakeUpTime = wakeUpTime;
        this.windDownTime = windDownTime;
        this.job = job;
        this.jobOtherDetail = jobOtherDetail;
        this.isStressedUnorganizedSchedule = isStressedUnorganizedSchedule;
        this.isForgetImportantThings = isForgetImportantThings;
        this.isPreferReminder = isPreferReminder;
        this.isImportantBreaks = isImportantBreaks;
    }

    public void update(
            final LocalTime wakeUpTime,
            final LocalTime windDownTime,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks)
    {
        this.wakeUpTime = wakeUpTime;
        this.windDownTime = windDownTime;
        this.job = job;
        this.jobOtherDetail = jobOtherDetail;
        this.isStressedUnorganizedSchedule = isStressedUnorganizedSchedule;
        this.isForgetImportantThings = isForgetImportantThings;
        this.isPreferReminder = isPreferReminder;
        this.isImportantBreaks = isImportantBreaks;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalTime getWakeUpTime() {
        return wakeUpTime;
    }

    public LocalTime getWindDownTime() {
        return windDownTime;
    }

    public JobType getJob() {
        return job;
    }

    public String getJobOtherDetail() {
        return jobOtherDetail;
    }

    public Boolean getIsStressedUnorganizedSchedule() {
        return isStressedUnorganizedSchedule;
    }

    public Boolean getIsForgetImportantThings() {
        return isForgetImportantThings;
    }

    public Boolean getIsPreferReminder() {
        return isPreferReminder;
    }

    public Boolean getIsImportantBreaks() {
        return isImportantBreaks;
    }
}