package com.official.memento.orderinfo.infrastructure.persistence;

import com.official.memento.orderinfo.infrastructure.persistence.projection.OrderInfoProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderInfoEntityJpaRepository extends JpaRepository<OrderInfoEntity, Long> {

    @Query("""
            SELECT o.id AS orderInfoId,
                   o.id AS memberId,
                   o.toDoId AS toDoId,
                   o.scheduleId AS scheduleId,
                   s.startDate AS startDate,
                   s.endDate AS endDate,
                   t.priorityValue AS priorityValue,
                   o.orderNum AS orderNum,
                   o.planType AS planType,
                   o.createdAt As createdAt
            FROM OrderInfoEntity o
            LEFT JOIN ToDoEntity t ON o.toDoId = t.id AND o.planType = 'TODO'
            LEFT JOIN ScheduleEntity s ON o.scheduleId = s.id AND o.planType = 'SCHEDULE'
            WHERE DATE(o.date) = :startDate AND o.memberId = :memberId
            ORDER BY o.orderNum ASC, o.createdAt ASC
            """)
    List<OrderInfoProjection> findOrderInfoWithDetails(final LocalDate startDate, final long memberId);

    void deleteByScheduleId(final long scheduleId);

    List<OrderInfoEntity> findAllByMemberIdAndDateOrderByOrderNum(final long memberId, final LocalDate date);

    void deleteByToDoId(final long toDoId);

    Optional<OrderInfoEntity> findByToDoId(Long toDoId);

    Optional<OrderInfoEntity> findByScheduleId(Long scheduleId);

    Optional<OrderInfoEntity> findByToDoIdAndDate(Long toDoId, LocalDate date);
}