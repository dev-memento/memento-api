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
        return entityToDomain(toDoEntity);
    }

    @Override
    public ToDo update(final ToDo toDo) {
        ToDoEntity toDoEntity = toDoJpaRepository.save(ToDoEntity.withId(toDo));
        return entityToDomain(toDoEntity);
    }

    @Override
    public ToDo findById(long toDoId) {
        ToDoEntity toDoEntity = toDoJpaRepository.findById(toDoId)
                .orElseThrow(
                        () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY)
                );
        return entityToDomain(toDoEntity);
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
                .map(this::entityToDomain)
                .toList();
    }

    @Override
    public List<ToDo> findAllByMemberIdAndStartDate(long memberId, LocalDate startDate) {
        List<ToDoEntity> toDoEntityList = toDoJpaRepository.findAllByMemberIdAndStartDate(memberId, startDate);
        return toDoEntityList.stream()
                .map(this::entityToDomain)
                .toList();
    }

    @Override
    public List<ToDo> findByMemberIdAndEndDateAndIsCompleted(final long memberId, final LocalDate endDate, final boolean isCompleted) {
        List<ToDoEntity> toDoEntityList = toDoJpaRepository.findByMemberIdAndEndDateAndIsCompleted(memberId, endDate, isCompleted);
        return toDoEntityList.stream()
                .map(this::entityToDomain)
                .toList();
    }

    // Entity를 Domain으로 변환하는 Helper 메서드
    private ToDo entityToDomain(final ToDoEntity entity) {
        return ToDo.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .groupId(entity.getGroupId())
                .startDate(entity.getStartDate())
                .description(entity.getDescription())
                .endDate(entity.getEndDate())
                .isCompleted(entity.isCompleted())
                .repeatOption(entity.getRepeatOption())
                .repeatExpiredDate(entity.getRepeatExpiredDate())
                .priorityUrgency(entity.getPriorityUrgency())
                .priorityImportance(entity.getPriorityImportance())
                .priorityValue(entity.getPriorityValue())
                .priorityType(entity.getPriorityType())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .tagId(entity.getTagId())
                .build();
    }
}
