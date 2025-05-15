package com.official.memento.member.infrastructure.persistence.entity;

import com.official.memento.member.domain.enums.JobType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "member_personal_info")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberPersonalInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long memberId;

    private LocalTime wakeUpTime;

    private LocalTime windDownTime;
    private String timeZoneOffset;

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
            final String timeZoneOffset,
            final JobType job,
            final String jobOtherDetail,
            final Boolean isStressedUnorganizedSchedule,
            final Boolean isForgetImportantThings,
            final Boolean isPreferReminder,
            final Boolean isImportantBreaks) {
        return new MemberPersonalInfoEntity(
                null, memberId, wakeUpTime, windDownTime, timeZoneOffset, job, jobOtherDetail,
                isStressedUnorganizedSchedule, isForgetImportantThings,
                isPreferReminder, isImportantBreaks
        );
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