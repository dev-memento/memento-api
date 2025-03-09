package com.official.memento.member.infrastructure.persistence.entity;

import com.official.memento.member.domain.enums.JobType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "member_personal_info")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPersonalInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long memberId;

    private LocalTime wakeUpTime;

    private LocalTime windDownTime;

    @Enumerated(EnumType.STRING)
    private JobType job;

    private String jobOtherDetail;

    private Boolean isStressedUnorganizedSchedule;

    private Boolean isForgetImportantThings;

    private Boolean isPreferReminder;

    private Boolean isImportantBreaks;

    public static MemberPersonalInfoEntity of(
            final long memberId,
            final LocalTime wakeUpTime,
            final LocalTime windDownTime,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks) {
        return new MemberPersonalInfoEntity(
                null, memberId, wakeUpTime, windDownTime, job, jobOtherDetail,
                isStressedUnorganizedSchedule, isForgetImportantThings,
                isPreferReminder, isImportantBreaks
        );
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
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

    // 추가된 updateFields 메서드
    public void updatePersonalInfo(
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
}