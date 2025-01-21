package com.official.memento.todo.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.domain.ToDoRepository;
import com.official.memento.todo.infrastructure.persistence.ToDoEntity;
import com.official.memento.todo.infrastructure.persistence.ToDoJpaRepository;
import com.official.memento.todo.infrastructure.persistence.ToDoTagJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Adapter
public class ToDoRepositoryAdapter implements ToDoRepository {

    private final ToDoJpaRepository toDoJpaRepository;
    private final ToDoTagJpaRepository toDoTagJpaRepository;

    public ToDoRepositoryAdapter(ToDoJpaRepository toDoJpaRepository, ToDoTagJpaRepository toDoTagJpaRepository) {
        this.toDoJpaRepository = toDoJpaRepository;
        this.toDoTagJpaRepository = toDoTagJpaRepository;
    }

    @Override
    public ToDo save(final ToDo toDo) {
        ToDoEntity toDoEntity = toDoJpaRepository.save(ToDoEntity.of(toDo));
        return ToDo.withId(
                toDoEntity.getId(),
                toDoEntity.getMemberId(),
                toDoEntity.getGroupId(),
                toDoEntity.getStartDate(),
                toDoEntity.getDescription(),
                toDoEntity.getEndDate(),
                toDoEntity.getIsCompleted(),
                toDoEntity.getRepeatOption(),
                toDoEntity.getRepeatExpiredDate(),
                toDoEntity.getPriorityUrgency(),
                toDoEntity.getPriorityImportance(),
                toDoEntity.getPriorityValue(),
                toDoEntity.getPriorityType(),
                toDoEntity.getType(),
                toDoEntity.getCreatedAt(),
                toDoEntity.getUpdatedAt()
        );
    }

    @Override
    public ToDo update(final ToDo toDo){
        ToDoEntity toDoEntity = toDoJpaRepository.save(ToDoEntity.withId(toDo));
        return ToDo.withId(
                toDoEntity.getId(),
                toDoEntity.getMemberId(),
                toDoEntity.getGroupId(),
                toDoEntity.getStartDate(),
                toDoEntity.getDescription(),
                toDoEntity.getEndDate(),
                toDoEntity.getIsCompleted(),
                toDoEntity.getRepeatOption(),
                toDoEntity.getRepeatExpiredDate(),
                toDoEntity.getPriorityUrgency(),
                toDoEntity.getPriorityImportance(),
                toDoEntity.getPriorityValue(),
                toDoEntity.getPriorityType(),
                toDoEntity.getType(),
                toDoEntity.getCreatedAt(),
                toDoEntity.getUpdatedAt()
        );
    }

    @Override
    public ToDo findById(long toDoId) {
        ToDoEntity toDoEntity = toDoJpaRepository.findById(toDoId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return ToDo.withId(
                toDoEntity.getId(),
                toDoEntity.getMemberId(),
                toDoEntity.getGroupId(),
                toDoEntity.getStartDate(),
                toDoEntity.getDescription(),
                toDoEntity.getEndDate(),
                toDoEntity.getIsCompleted(),
                toDoEntity.getRepeatOption(),
                toDoEntity.getRepeatExpiredDate(),
                toDoEntity.getPriorityUrgency(),
                toDoEntity.getPriorityImportance(),
                toDoEntity.getPriorityValue(),
                toDoEntity.getPriorityType(),
                toDoEntity.getType(),
                toDoEntity.getCreatedAt(),
                toDoEntity.getUpdatedAt()
        );
    }

    @Override
    public void deleteById(final long toDoId) {
        toDoJpaRepository.deleteById(toDoId);
    }


    @Override
    public List<ToDo> findAllByMemberId(long memberId) {
        List<ToDoEntity> toDoEntityList = toDoJpaRepository.findAllByMemberId(memberId);
        return toDoEntityList.stream()
                .map(
                        t -> ToDo.withId(
                                t.getId(),
                                t.getMemberId(),
                                t.getGroupId(),
                                t.getStartDate(),
                                t.getDescription(),
                                t.getEndDate(),
                                t.getIsCompleted(),
                                t.getRepeatOption(),
                                t.getRepeatExpiredDate(),
                                t.getPriorityUrgency(),
                                t.getPriorityImportance(),
                                t.getPriorityValue(),
                                t.getPriorityType(),
                                t.getType(),
                                t.getCreatedAt(),
                                t.getUpdatedAt()
                        )
                ).toList();
    }
}
