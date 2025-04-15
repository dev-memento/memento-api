package com.official.memento.todo.infrastructure.persistence;

import com.official.memento.todo.infrastructure.persistence.projection.ToDoWithOrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ToDoJpaRepository extends JpaRepository<ToDoEntity, Long> {
    List<ToDoEntity> findAllByMemberId(long memberId);

    List<ToDoEntity> findAllByMemberIdAndStartDate(long memberId, LocalDate startDate);

    List<ToDoEntity> findByMemberIdAndEndDateAndIsCompleted(final long memberId, final LocalDate endDate, final boolean isCompleted);

    void deleteAllByMemberId(final long memberId);
}
