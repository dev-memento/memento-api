package com.official.memento.todo.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ToDoJpaRepository extends JpaRepository<ToDoEntity, Long> {
    List<ToDoEntity> findAllByMemberId(long memberId);
    List<ToDoEntity> findAllByMemberIdAndStartDate(long memberId, LocalDate startDate);
}
