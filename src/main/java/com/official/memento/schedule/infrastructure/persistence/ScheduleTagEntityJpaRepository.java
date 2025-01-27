package com.official.memento.schedule.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleTagEntityJpaRepository extends JpaRepository<ScheduleTagEntity, Long> {
    void deleteByScheduleId(final long scheduleId);
    Optional<ScheduleTagEntity> findByScheduleId(final long scheduleId);
}
