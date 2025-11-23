package com.official.memento.todo.infrastructure;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.repository.ToDoRepository;
import com.official.memento.todo.infrastructure.persistence.ToDoEntity;
import com.official.memento.todo.infrastructure.persistence.ToDoJpaRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class ToDoRepositoryAdapter implements ToDoRepository {

    private final ToDoJpaRepository toDoJpaRepository;

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
                toDoEntity.isCompleted(),
                toDoEntity.getRepeatOption(),
                toDoEntity.getRepeatExpiredDate(),
                toDoEntity.getPriorityUrgency(),
                toDoEntity.getPriorityImportance(),
                toDoEntity.getPriorityValue(),
                toDoEntity.getPriorityType(),
                toDoEntity.getType(),
                toDoEntity.getCreatedAt(),
                toDoEntity.getUpdatedAt(),
                toDoEntity.getTagId()
        );
    }

    @Override
    public ToDo update(final ToDo toDo) {
        ToDoEntity toDoEntity = toDoJpaRepository.save(ToDoEntity.withId(toDo));
        return ToDo.withId(
                toDoEntity.getId(),
                toDoEntity.getMemberId(),
                toDoEntity.getGroupId(),
                toDoEntity.getStartDate(),
                toDoEntity.getDescription(),
                toDoEntity.getEndDate(),
                toDoEntity.isCompleted(),
                toDoEntity.getRepeatOption(),
                toDoEntity.getRepeatExpiredDate(),
                toDoEntity.getPriorityUrgency(),
                toDoEntity.getPriorityImportance(),
                toDoEntity.getPriorityValue(),
                toDoEntity.getPriorityType(),
                toDoEntity.getType(),
                toDoEntity.getCreatedAt(),
                toDoEntity.getUpdatedAt(),
                toDoEntity.getTagId()
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
                toDoEntity.isCompleted(),
                toDoEntity.getRepeatOption(),
                toDoEntity.getRepeatExpiredDate(),
                toDoEntity.getPriorityUrgency(),
                toDoEntity.getPriorityImportance(),
                toDoEntity.getPriorityValue(),
                toDoEntity.getPriorityType(),
                toDoEntity.getType(),
                toDoEntity.getCreatedAt(),
                toDoEntity.getUpdatedAt(),
                toDoEntity.getTagId()
        );
    }

    @Override
    public void deleteById(final long toDoId) {
        toDoJpaRepository.deleteById(toDoId);
    }

    @Override
    public void deleteAllByMemberId(final long memberId) {
        toDoJpaRepository.deleteAllByMemberId(memberId);
    }

    @Override
    public void updateTagForTodo(final long oldTagId, final long newTagId){
        toDoJpaRepository.updateTagForTodo(oldTagId, newTagId);
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
                                t.isCompleted(),
                                t.getRepeatOption(),
                                t.getRepeatExpiredDate(),
                                t.getPriorityUrgency(),
                                t.getPriorityImportance(),
                                t.getPriorityValue(),
                                t.getPriorityType(),
                                t.getType(),
                                t.getCreatedAt(),
                                t.getUpdatedAt(),
                                t.getTagId()
                        )
                ).toList();
    }

    @Override
    public List<ToDo> findAllByMemberIdAndStartDate(long memberId, LocalDate startDate) {
        List<ToDoEntity> toDoEntityList = toDoJpaRepository.findAllByMemberIdAndStartDate(memberId, startDate);
        return toDoEntityList.stream()
                .map(
                        t -> ToDo.withId(
                                t.getId(),
                                t.getMemberId(),
                                t.getGroupId(),
                                t.getStartDate(),
                                t.getDescription(),
                                t.getEndDate(),
                                t.isCompleted(),
                                t.getRepeatOption(),
                                t.getRepeatExpiredDate(),
                                t.getPriorityUrgency(),
                                t.getPriorityImportance(),
                                t.getPriorityValue(),
                                t.getPriorityType(),
                                t.getType(),
                                t.getCreatedAt(),
                                t.getUpdatedAt(),
                                t.getTagId()
                        )
                ).toList();
    }

    @Override
    public List<ToDo> findByMemberIdAndEndDateAndIsCompleted(final long memberId, final LocalDate endDate, final boolean isCompleted) {
        List<ToDoEntity> toDoEntityList = toDoJpaRepository.findByMemberIdAndEndDateAndIsCompleted(memberId, endDate, isCompleted);
        return toDoEntityList.stream()
                .map(
                        t -> ToDo.withId(
                                t.getId(),
                                t.getMemberId(),
                                t.getGroupId(),
                                t.getStartDate(),
                                t.getDescription(),
                                t.getEndDate(),
                                t.isCompleted(),
                                t.getRepeatOption(),
                                t.getRepeatExpiredDate(),
                                t.getPriorityUrgency(),
                                t.getPriorityImportance(),
                                t.getPriorityValue(),
                                t.getPriorityType(),
                                t.getType(),
                                t.getCreatedAt(),
                                t.getUpdatedAt(),
                                t.getTagId()
                        )
                ).toList();
    }
}
