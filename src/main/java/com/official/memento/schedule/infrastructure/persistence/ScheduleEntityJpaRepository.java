package com.official.memento.schedule.infrastructure.persistence;

import com.official.memento.schedule.domain.enums.ScheduleType;
import com.official.memento.schedule.infrastructure.persistence.projection.ScheduleOrderInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleEntityJpaRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findAllByScheduleGroupIdAndStartDateGreaterThanEqual(final String groupId, final LocalDateTime startDate);

    ScheduleEntity findByScheduleGroupId(final String groupId);

    @Query("""
    SELECT s.id AS scheduleId,
           s.memberId AS memberId,
           s.description AS description,
           s.startDate AS startDate,
           s.endDate AS endDate,
           s.isAllDay AS isAllDay,
           s.repeatOption AS repeatOption,
           s.repeatExpiredDate AS repeatExpiredDate,
           s.type AS type,
           s.scheduleGroupId AS scheduleGroupId,
           o.orderNum AS orderNum,
           t.name AS tagName,
           t.color AS tagColor,
           t.id AS tagId
    FROM ScheduleEntity s
    LEFT JOIN OrderInfoEntity o ON s.id = o.scheduleId
    LEFT JOIN TagEntity t ON s.tagId = t.id
    WHERE s.isAllDay = false
      AND s.memberId = :memberId
    ORDER BY s.startDate ASC, o.orderNum ASC
    """)
    List<ScheduleOrderInfoProjection> findNonAllDaySchedulesWithOrderInfo(final long memberId);

    @Query("""
    SELECT s.id AS scheduleId,
           s.memberId AS memberId,
           s.description AS description,
           s.startDate AS startDate,
           s.endDate AS endDate,
           s.isAllDay AS isAllDay,
           s.repeatOption AS repeatOption,
           s.repeatExpiredDate AS repeatExpiredDate,
           s.type AS type,
           s.scheduleGroupId AS scheduleGroupId,
           s.createdAt AS createdAt,
           s.updatedAt As updatedAt,
           t.name AS tagName,
           t.color AS tagColor,
           t.id AS tagId
    FROM ScheduleEntity s
    LEFT JOIN TagEntity t ON s.tagId = t.id
    WHERE s.isAllDay = true
      AND s.memberId = :memberId
    ORDER BY s.startDate ASC, s.createdAt ASC
    """)
    List<ScheduleOrderInfoProjection> findAllDaySchedulesByMemberIdOrderedByStartDate(final long memberId);

    @Query("""
    SELECT s.id AS scheduleId,
           s.memberId AS memberId,
           s.description AS description,
           s.startDate AS startDate,
           s.endDate AS endDate,
           s.isAllDay AS isAllDay,
           s.repeatOption AS repeatOption,
           s.repeatExpiredDate AS repeatExpiredDate,
           s.type AS type,
           s.scheduleGroupId AS scheduleGroupId,
           o.orderNum AS orderNum,
           t.name AS tagName,
           t.color AS tagColor,
           t.id AS tagId
    FROM ScheduleEntity s
    LEFT JOIN OrderInfoEntity o ON s.id = o.scheduleId
    LEFT JOIN TagEntity t ON s.tagId = t.id
    WHERE s.memberId = :memberId
      AND DATE(s.startDate) = :date
    ORDER BY o.orderNum ASC
    """)
    List<ScheduleOrderInfoProjection> findSchedulesByMemberIdAndDateOrderedByOrderNum(
            long memberId,
            LocalDate date
    );

    List<ScheduleEntity> findAllByMemberIdAndType(long memberId, ScheduleType type);

    void deleteAllByScheduleGroupId(final String groupId);
}
