package com.official.memento.todo.service;

import static com.official.memento.todo.domain.entity.enums.ToDoType.NORMAL;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.tag.service.result.TagResult;
import com.official.memento.tag.service.usecase.TagGetUseCase;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.repository.ToDoRepository;
import com.official.memento.todo.service.command.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.official.memento.todo.service.usecase.ToDoCreateUseCase;
import com.official.memento.todo.service.usecase.ToDoDeleteUseCase;
import com.official.memento.todo.service.usecase.ToDoUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ToDoService implements ToDoCreateUseCase, ToDoDeleteUseCase, ToDoUpdateUseCase {

    private final ToDoRepository toDoRepository;
    private final TagGetUseCase tagGetUseCase;
    private final OrderInfoDeleteUseCase orderInfoDeleteUseCase;
    private final OrderInfoCreateUseCase orderInfoCreateUseCase;

    @Override
    @Transactional
    public ToDo create(final ToDoCreateCommand command) {
        String toDoGroupId = createGroupId();
        return createSingleToDo(command, toDoGroupId);
    }

    @Override
    @Transactional
    public void delete(final ToDoDeleteCommand toDoDeleteCommand) {
        ToDo toDo = toDoRepository.findById(toDoDeleteCommand.toDoId());
        toDo.checkOwn(toDoDeleteCommand.memberId());
        toDoRepository.deleteById(toDo.getId());
        orderInfoDeleteUseCase.deleteByToDoId(toDo.getId());
    }

    @Override
    public void deleteAllByMemberId(final long memberId) {
        List<ToDo> toDos = toDoRepository.findAllByMemberId(memberId);
        for (ToDo toDo : toDos) {
            orderInfoDeleteUseCase.deleteByToDoId(toDo.getId());
        }
        toDoRepository.deleteAllByMemberId(memberId);
    }

    @Override
    @Transactional
    public ToDo update(final ToDoUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        toDo.checkOwn(command.memberId());

        if (command.tagId() != toDo.getTagId()) {
            checkOwnTag(command.memberId(), tagGetUseCase.findById(command.tagId()));
        }

        ToDo updatedTodo = updateTodo(command, toDo);

        if (toDo.getStartDate() != command.startDate()) {
            orderInfoDeleteUseCase.deleteByToDoId(updatedTodo.getId());
            orderInfoCreateUseCase.assignToDoOrder(command.startDate(), updatedTodo.getId(), command.memberId());
        }
        return updatedTodo;
    }

    @Override
    public void updateTodosTag(TodosTagUpdateCommand command) {
        toDoRepository.updateTagForTodo(command.currentId(), command.newId());
    }

    @Override
    @Transactional
    public boolean updateCompletion(final ToDoCompletionUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        toDo.checkOwn(command.memberId());
        ToDo updated = toDo.toBuilder()
                .isCompleted(!toDo.isCompleted())
                .updatedAt(LocalDateTime.now())
                .build();
        return toDoRepository.update(updated).isCompleted();
    }

    private ToDo createSingleToDo(final ToDoCreateCommand command, final String toDoGroupId) {
        TagResult tagResult = tagGetUseCase.findById(command.tagId());
        checkOwnTag(command.memberId(), tagResult);
        ToDo toDo = createToDo(command, toDoGroupId);
        orderInfoCreateUseCase.assignToDoOrder(command.startDate(), toDo.getId(), command.memberId());
        return toDo;
    }

    private ToDo updateTodo(final ToDoUpdateCommand command, final ToDo toDo) {
        ToDo updatedTodo = toDo.update(
                command.startDate(),
                command.description(),
                command.endDate(),
                command.priorityUrgency(),
                command.priorityImportance(),
                command.tagId()
        );
        toDoRepository.update(updatedTodo);
        return updatedTodo;
    }

    private static void checkOwnTag(long memberId, TagResult tagResult) {
        if (tagResult.memberId() != memberId) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_REQUEST_BODY);
        }
    }

    private ToDo createToDo(
            final ToDoCreateCommand command,
            final String toDoGroupId
    ) {
        return toDoRepository.save(ToDo.of(
                command.memberId(),
                toDoGroupId,
                command.startDate(),
                command.description(),
                command.endDate(),
                false,
                command.repeatOption(),
                command.repeatExpiredDate(),
                command.priorityUrgency(),
                command.priorityImportance(),
                NORMAL,
                command.tagId()
        ));
    }

    private String createGroupId() {
        return UUID.randomUUID().toString();
    }
}
