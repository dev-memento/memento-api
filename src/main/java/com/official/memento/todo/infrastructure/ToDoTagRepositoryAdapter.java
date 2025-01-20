package com.official.memento.todo.infrastructure;

import com.official.memento.global.stereotype.Adapter;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.domain.ToDoTag;
import com.official.memento.todo.domain.ToDoTagRepository;
import com.official.memento.todo.infrastructure.persistence.ToDoEntity;
import com.official.memento.todo.infrastructure.persistence.ToDoTagEntity;
import com.official.memento.todo.infrastructure.persistence.ToDoTagJpaRepository;

import java.util.Optional;

@Adapter
public class ToDoTagRepositoryAdapter implements ToDoTagRepository {

    private final ToDoTagJpaRepository toDoTagJpaRepository;

    public ToDoTagRepositoryAdapter(ToDoTagJpaRepository toDoTagJpaRepository){
        this.toDoTagJpaRepository = toDoTagJpaRepository;
    }

    @Override
    public ToDoTag save(ToDoTag toDoTag){
        ToDoTagEntity toDoTagEntity = toDoTagJpaRepository.save(ToDoTagEntity.of(toDoTag));

        return ToDoTag.withId(
                toDoTagEntity.getId(),
                toDoTag.getTagId(),
                toDoTag.getToDoId(),
                toDoTag.getCreatedAt(),
                toDoTag.getUpdatedAt()
        );
    }

    @Override
    public ToDoTag update(final ToDoTag toDoTag) {
        ToDoTagEntity toDoTagEntity = toDoTagJpaRepository.save(ToDoTagEntity.withId(toDoTag));

        return ToDoTag.withId(
                toDoTagEntity.getId(),
                toDoTagEntity.getTagId(),
                toDoTagEntity.getToDoId(),
                toDoTagEntity.getCreatedAt(),
                toDoTagEntity.getUpdatedAt()
        );
    }

    @Override
    public ToDoTag findByToDoId(final long toDoId){
        Optional<ToDoTagEntity> toDoTagEntity = toDoTagJpaRepository.findByToDoId(toDoId);
        return toDoTagEntity.map(toDoTag -> ToDoTag.withId(
                toDoTag.getId(),
                toDoTag.getTagId(),
                toDoTag.getToDoId(),
                toDoTag.getCreatedAt(),
                toDoTag.getUpdatedAt()
        )).orElse(null);
    }

    @Override
    public void deleteByToDoId(long toDoId){
        toDoTagJpaRepository.deleteByToDoId(toDoId);
    }
}
