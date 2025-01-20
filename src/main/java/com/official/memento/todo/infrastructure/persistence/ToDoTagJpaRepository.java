package com.official.memento.todo.infrastructure.persistence;

import com.official.memento.tag.infrastructure.persistence.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToDoTagJpaRepository extends JpaRepository<ToDoTagEntity, Long> {

    void deleteByToDoId(final long toDoId);

    Optional<ToDoTagEntity> findByToDoId(final long toDoId);
}
