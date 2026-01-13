package com.official.memento.schedule.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.schedule.infrastructure.persistence.ScheduleEntity;
import com.official.memento.schedule.infrastructure.persistence.ScheduleEntityJpaRepository;
import com.official.memento.schedule.infrastructure.persistence.projection.ScheduleOrderInfoProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ScheduleRepositoryAdapter implements ScheduleRepository {

    private final ScheduleEntityJpaRepository scheduleEntityJpaRepository;

    @Override
    public Schedule save(final Schedule schedule) {
        ScheduleEntity scheduleEntity = scheduleEntityJpaRepository.save(ScheduleEntity.of(schedule));
        return Schedule.builder()
                .id(scheduleEntity.getId())
                .memberId(scheduleEntity.getMemberId())
                .description(scheduleEntity.getDescription())
                .startDate(scheduleEntity.getStartDate())
                .endDate(scheduleEntity.getEndDate())
                .isAllDay(scheduleEntity.isAllDay())
                .repeatOption(scheduleEntity.getRepeatOption())
                .repeatExpiredDate(scheduleEntity.getRepeatExpiredDate())
                .type(scheduleEntity.getType())
                .scheduleGroupId(scheduleEntity.getScheduleGroupId())
                .createdAt(scheduleEntity.getCreatedAt())
                .updatedAt(scheduleEntity.getUpdatedAt())
                .tagId(scheduleEntity.getTagId())
                .build();
    }

    @Override
    public Schedule update(final Schedule schedule) {
        ScheduleEntity scheduleEntity = scheduleEntityJpaRepository.save(ScheduleEntity.from(schedule));
        return Schedule.builder()
                .id(scheduleEntity.getId())
                .memberId(scheduleEntity.getMemberId())
                .description(scheduleEntity.getDescription())
                .startDate(scheduleEntity.getStartDate())
                .endDate(scheduleEntity.getEndDate())
                .isAllDay(scheduleEntity.isAllDay())
                .repeatOption(scheduleEntity.getRepeatOption())
                .repeatExpiredDate(scheduleEntity.getRepeatExpiredDate())
                .type(scheduleEntity.getType())
                .scheduleGroupId(scheduleEntity.getScheduleGroupId())
                .createdAt(scheduleEntity.getCreatedAt())
                .updatedAt(scheduleEntity.getUpdatedAt())
                .tagId(scheduleEntity.getTagId())
                .build();
    }

    @Override
    public Schedule findById(long scheduleId) {
        ScheduleEntity scheduleEntity = scheduleEntityJpaRepository.findById(scheduleId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return Schedule.builder()
                .id(scheduleEntity.getId())
                .memberId(scheduleEntity.getMemberId())
                .description(scheduleEntity.getDescription())
                .startDate(scheduleEntity.getStartDate())
                .endDate(scheduleEntity.getEndDate())
                .isAllDay(scheduleEntity.isAllDay())
                .repeatOption(scheduleEntity.getRepeatOption())
                .repeatExpiredDate(scheduleEntity.getRepeatExpiredDate())
                .type(scheduleEntity.getType())
                .scheduleGroupId(scheduleEntity.getScheduleGroupId())
                .createdAt(scheduleEntity.getCreatedAt())
                .updatedAt(scheduleEntity.getUpdatedAt())
                .tagId(scheduleEntity.getTagId())
                .build();
    }


    @Override
    public List<Schedule> findNonAllDaySchedulesWithOrderInfo(long memberId) {
        List<ScheduleOrderInfoProjection> schedulesWithOrderInfo = scheduleEntityJpaRepository
                .findNonAllDaySchedulesWithOrderInfo(memberId);
        return schedulesWithOrderInfo.stream()
                .map(s -> Schedule.builder()
                        .id(s.getScheduleId())
                        .memberId(s.getMemberId())
                        .description(s.getDescription())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .isAllDay(s.getIsAllDay())
                        .repeatOption(s.getRepeatOption())
                        .repeatExpiredDate(s.getRepeatExpiredDate())
                        .type(s.getType())
                        .scheduleGroupId(s.getScheduleGroupId())
                        .createdAt(s.getCreatedAt())
                        .updatedAt(s.getUpdatedAt())
                        .orderNum(s.getOrderNum())  // Order 정보 포함
                        .tagName(s.getTagName())    // Tag 정보 포함
                        .tagColor(s.getTagColor())  // Tag 정보 포함
                        .tagId(s.getTagId())
                        .build())
                .toList();
    }

    @Override
    public List<Schedule> findAllAlDaysByMemberId(long memberId) {
        List<ScheduleOrderInfoProjection> scheduleEntities = scheduleEntityJpaRepository.findAllDaySchedulesByMemberIdOrderedByStartDate(
                memberId);
        return scheduleEntities.stream()
                .map(s -> Schedule.builder()
                        .id(s.getScheduleId())
                        .memberId(s.getMemberId())
                        .description(s.getDescription())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .isAllDay(s.getIsAllDay())
                        .repeatOption(s.getRepeatOption())
                        .repeatExpiredDate(s.getRepeatExpiredDate())
                        .type(s.getType())
                        .scheduleGroupId(s.getScheduleGroupId())
                        .createdAt(s.getEndDate())
                        .updatedAt(s.getUpdatedAt())
                        .tagName(s.getTagName())   // Tag 정보 포함
                        .tagColor(s.getTagColor()) // Tag 정보 포함
                        .tagId(s.getTagId())
                        .build())
                .toList();
    }

    @Override
    public List<Schedule> findAllByStartDateAndMemberId(final LocalDate startDate, final long memberId) {
        List<ScheduleOrderInfoProjection> schedules = scheduleEntityJpaRepository.findSchedulesByMemberIdAndDateOrderedByOrderNum(
                memberId, startDate);
        return schedules.stream()
                .map(s -> Schedule.builder()
                        .id(s.getScheduleId())
                        .memberId(s.getMemberId())
                        .description(s.getDescription())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .isAllDay(s.getIsAllDay())
                        .repeatOption(s.getRepeatOption())
                        .repeatExpiredDate(s.getRepeatExpiredDate())
                        .type(s.getType())
                        .scheduleGroupId(s.getScheduleGroupId())
                        .createdAt(s.getCreatedAt())
                        .updatedAt(s.getUpdatedAt())
                        .orderNum(s.getOrderNum())  // Order 정보 포함
                        .tagName(s.getTagName())    // Tag 정보 포함
                        .tagColor(s.getTagColor())  // Tag 정보 포함
                        .tagId(s.getTagId())
                        .build())
                .toList();
    }

    @Override
    public List<Schedule> findAllAppleByMemberId(final long memberId) {
        List<ScheduleEntity> scheduleEntities = scheduleEntityJpaRepository.findAllByMemberIdAndType(memberId,
                ScheduleType.APPLE);
        return scheduleEntities.stream()
                .map(entity -> Schedule.builder()
                        .id(entity.getId())
                        .memberId(entity.getMemberId())
                        .description(entity.getDescription())
                        .startDate(entity.getStartDate())
                        .endDate(entity.getEndDate())
                        .isAllDay(entity.isAllDay())
                        .repeatOption(entity.getRepeatOption())
                        .repeatExpiredDate(entity.getRepeatExpiredDate())
                        .type(entity.getType())
                        .scheduleGroupId(entity.getScheduleGroupId())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .tagId(entity.getTagId())
                        .build())
                .toList();
    }

    @Override
    public List<Schedule> findAllByMemberId(final long memberId) {
        List<ScheduleEntity> scheduleEntities = scheduleEntityJpaRepository.findAllByMemberId(memberId);
        return scheduleEntities.stream()
                .map(entity -> Schedule.builder()
                        .id(entity.getId())
                        .memberId(entity.getMemberId())
                        .description(entity.getDescription())
                        .startDate(entity.getStartDate())
                        .endDate(entity.getEndDate())
                        .isAllDay(entity.isAllDay())
                        .repeatOption(entity.getRepeatOption())
                        .repeatExpiredDate(entity.getRepeatExpiredDate())
                        .type(entity.getType())
                        .scheduleGroupId(entity.getScheduleGroupId())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .tagId(entity.getTagId())
                        .build())
                .toList();
    }

    @Override
    public Optional<Schedule> findByScheduleGroupIdOrNull(final String scheduleGroupId) {
        Optional<ScheduleEntity> scheduleEntity = scheduleEntityJpaRepository.findByScheduleGroupId(scheduleGroupId);
        return scheduleEntity.map(entity -> Schedule.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isAllDay(entity.isAllDay())
                .repeatOption(entity.getRepeatOption())
                .repeatExpiredDate(entity.getRepeatExpiredDate())
                .type(entity.getType())
                .scheduleGroupId(entity.getScheduleGroupId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .tagId(entity.getTagId())
                .build());
    }

    @Override
    public void updateTagForSchedules(final long oldTagId, final long newTagId){
        scheduleEntityJpaRepository.updateTagForSchedules(oldTagId, newTagId);
    }

    @Override
    public void deleteById(final long scheduleId) {
        scheduleEntityJpaRepository.deleteById(scheduleId);
    }

    @Override
    public void deleteAllByMemberId(final long memberId) {
        scheduleEntityJpaRepository.deleteAllByMemberId(memberId);
    }

    @Override
    public void deleteAllByScheduleGroupId(final String scheduleGroupId) {
        scheduleEntityJpaRepository.deleteAllByScheduleGroupId(scheduleGroupId);
    }

}
