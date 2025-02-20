package com.official.memento.todo.service;

import static com.official.memento.global.entity.enums.RepeatOption.NONE;
import static com.official.memento.todo.domain.entity.enums.ToDoType.NORMAL;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.global.exception.MementoException;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.orderinfo.service.usecase.OrderInfoCreateUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoDeleteUseCase;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.repository.ToDoRepository;
import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoCreateCommand;
import com.official.memento.todo.service.command.ToDoDeleteCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ToDoService implements ToDoCreateUseCase, ToDoDeleteUseCase, ToDoUpdateUseCase {

    private final ToDoRepository toDoRepository;
    private final TagRepository tagRepository;
    private final OrderInfoDeleteUseCase orderInfoDeleteUseCase;
    private final OrderInfoCreateUseCase orderInfoCreateUseCase;

    @Override
    @Transactional
    public void create(final ToDoCreateCommand command) {
        String toDoGroupId = createGroupId();
        if (command.repeatOption() == NONE) {
            createSingleToDo(command, toDoGroupId);
        } else {
            throw new MementoException(ErrorCode.INVALID_REQUEST_BODY);
            /*
            반복 기능 미구현으로 인한 주석 처리
            createRepeatToDos(command, toDoGroupId);
             */
        }
    }

    @Override
    @Transactional
    public void delete(final ToDoDeleteCommand toDoDeleteCommand) {
        ToDo toDo = toDoRepository.findById(toDoDeleteCommand.toDoId());
        checkOwn(toDoDeleteCommand.memberId(), toDo);
        toDoRepository.deleteById(toDo.getId());
        orderInfoDeleteUseCase.deleteByToDoId(toDo.getId());
    }

    @Override
    @Transactional
    public void update(final ToDoUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);

        if (command.tagId() != toDo.getTagId()) {
            checkOwnTag(command.memberId(), tagRepository.findById(command.tagId()));
        }

        PriorityType newPriorityType = toDo.getPriorityType();
        Double newPriorityValue = toDo.getPriorityValue();
        if (!Objects.equals(command.priorityUrgency(), toDo.getPriorityUrgency()) || !Objects.equals(
                command.priorityImportance(), toDo.getPriorityImportance())) {
            newPriorityType = determinePriorityType(command.priorityUrgency(), command.priorityImportance());
            newPriorityValue = calculatePriorityValue(command.priorityUrgency(), command.priorityImportance());
        }

        updateTodo(command, toDo, newPriorityType, newPriorityValue);

        if (toDo.getStartDate() != command.startDate()) {
            orderInfoDeleteUseCase.deleteByToDoId(toDo.getId());
            orderInfoCreateUseCase.assignToDoOrder(command.startDate(), toDo, command.memberId());
        }
    }

    private void updateTodo(
            final ToDoUpdateCommand command,
            final ToDo toDo,
            final PriorityType newPriorityType,
            final Double newPriorityValue
    ) {
        toDoRepository.update(toDo.update(
                command.startDate(),
                command.description(),
                command.endDate(),
                command.priorityUrgency(),
                command.priorityImportance(),
                null,
                newPriorityType,
                newPriorityValue,
                command.tagId()
        ));
    }

    @Override
    @Transactional
    public boolean updateCompletion(final ToDoCompletionUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);
        toDo.updateCompletion(!toDo.isCompleted());
        return toDoRepository.update(toDo).isCompleted();
    }

    private void createSingleToDo(final ToDoCreateCommand command, final String toDoGroupId) {
        Double priorityValue = calculatePriorityValue(command.priorityUrgency(), command.priorityImportance());
        PriorityType priorityType = determinePriorityType(command.priorityUrgency(), command.priorityImportance());
        Tag tag = tagRepository.findById(command.tagId());
        checkOwnTag(command.memberId(), tag);
        ToDo toDo = createToDo(command, toDoGroupId, priorityValue, priorityType, command.startDate());
        orderInfoCreateUseCase.assignToDoOrder(command.startDate(), toDo, command.memberId());
    }

    private static void checkOwnTag(long memberId, Tag tag) {
        if (tag.getMemberId() != memberId) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_REQUEST_BODY);
        }
    }

    private void createRepeatToDos(final ToDoCreateCommand command, final String toDoGroupId) {
        LocalDate currentDate = command.startDate();
        LocalDate repeatExpiredDate = command.repeatExpiredDate();

        tagRepository.findById(command.tagId());

        while (!currentDate.isAfter(repeatExpiredDate)) {
            Double priorityValue = calculatePriorityValue(command.priorityUrgency(), command.priorityImportance());
            PriorityType priorityType = determinePriorityType(command.priorityUrgency(), command.priorityImportance());

            ToDo toDo = createToDo(command, toDoGroupId, priorityValue, priorityType, currentDate);

            if (command.repeatOption() == RepeatOption.DAILY) {
                currentDate = currentDate.plusDays(1);
            } else if (command.repeatOption() == RepeatOption.WEEKLY) {
                currentDate = currentDate.plusWeeks(1);
            } else if (command.repeatOption() == RepeatOption.MONTHLY) {
                currentDate = currentDate.plusMonths(1);
            } else {
                currentDate = currentDate.plusYears(1);
            }

            orderInfoCreateUseCase.assignToDoOrder(command.startDate(), toDo, command.memberId());
        }
    }

    private ToDo createToDo(
            final ToDoCreateCommand command,
            final String toDoGroupId,
            final Double priorityValue,
            final PriorityType priorityType,
            final LocalDate startDate
    ) {
        return toDoRepository.save(ToDo.of(
                command.memberId(),
                toDoGroupId,
                startDate,
                command.description(),
                command.endDate(),
                false,
                command.repeatOption(),
                command.repeatExpiredDate(),
                command.priorityUrgency(),
                command.priorityImportance(),
                priorityValue,
                priorityType,
                NORMAL,
                command.tagId()
        ));
    }

    private String createGroupId() {
        return UUID.randomUUID().toString();
    }

    private Double calculatePriorityValue(Double priorityUrgency, Double priorityImportance) {
        if (priorityUrgency != null && priorityImportance != null) {
            return (priorityUrgency * 0.3) + (priorityImportance * 0.7);
        } else {
            return -1.0;
        }
    }

    private PriorityType determinePriorityType(Double priorityUrgency, Double priorityImportance) {
        return PriorityType.findPriorityType(priorityUrgency, priorityImportance);
    }

    private static void checkOwn(final long memberId, final ToDo toDo) {
        if (toDo.getMemberId() != memberId) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}
