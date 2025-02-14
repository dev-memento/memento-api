package com.official.memento.todo.service;

import static com.official.memento.global.entity.enums.RepeatOption.NONE;
import static com.official.memento.todo.domain.entity.enums.ToDoType.NORMAL;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.entity.enums.PriorityType;
import com.official.memento.todo.domain.repository.ToDoRepository;
import com.official.memento.todo.service.command.ToDoCompletionUpdateCommand;
import com.official.memento.todo.service.command.ToDoCreateCommand;
import com.official.memento.todo.service.command.ToDoDeleteCommand;
import com.official.memento.todo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.todo.service.command.ToDoUpdateCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private final OrderInfoRepository orderInfoRepository;

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
        orderInfoRepository.deleteByToDoId(toDo.getId());
    }

    @Override
    @Transactional
    public void update(final ToDoUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);

        if (command.tagId() != toDo.getTagId()) {
            tagRepository.findById(command.tagId());
        }

        PriorityType newPriorityType = toDo.getPriorityType();
        Double newPriorityValue = toDo.getPriorityValue();
        if (!Objects.equals(command.priorityUrgency(), toDo.getPriorityUrgency()) || !Objects.equals(
                command.priorityImportance(), toDo.getPriorityImportance())) {
            newPriorityType = determinePriorityType(command.priorityUrgency(), command.priorityImportance());
            newPriorityValue = calculatePriorityValue(command.priorityUrgency(), command.priorityImportance());
        }

        updateTodo(command, toDo, newPriorityType, newPriorityValue);

        if (toDo.getStartDate() != command.startDate() || toDo.getEndDate() != command.endDate()) {
            orderInfoRepository.deleteByToDoId(toDo.getId());
            assignOrder(command.startDate(), toDo,command.memberId());
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

    @Override
    @Transactional
    public void updatePosition(final ToDoPositionUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);

        LocalDate date = orderInfoRepository.findDateByToDoId(command.toDoId());
        double currentOrderNum = orderInfoRepository.findOrderByToDoId(command.toDoId());
        double targetOrderNum = command.targetOrderNum();

        if (currentOrderNum > targetOrderNum) {
            List<OrderInfo> ordersToUpdate = orderInfoRepository.findOrdersBetween(date, targetOrderNum,
                    currentOrderNum - 1.0);
            for (OrderInfo order : ordersToUpdate) {
                order.incrementOrder();
                orderInfoRepository.update(order);
            }
        } else if (currentOrderNum < targetOrderNum) {
            List<OrderInfo> ordersToUpdate = orderInfoRepository.findOrdersBetween(date, currentOrderNum + 1,
                    targetOrderNum);
            for (OrderInfo order : ordersToUpdate) {
                order.decrementOrder();
                orderInfoRepository.update(order);
            }
        }

        OrderInfo orderInfo = orderInfoRepository.findByToDoId(command.toDoId());
        orderInfo.setOrderNum(targetOrderNum);
        orderInfoRepository.update(orderInfo);
    }


    private void createSingleToDo(final ToDoCreateCommand command, final String toDoGroupId) {
        Double priorityValue = calculatePriorityValue(command.priorityUrgency(), command.priorityImportance());
        PriorityType priorityType = determinePriorityType(command.priorityUrgency(), command.priorityImportance());
        tagRepository.findById(command.tagId());
        ToDo toDo = createToDo(command, toDoGroupId, priorityValue, priorityType, command.startDate());
        assignOrder(command.startDate(), toDo,command.memberId());
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

            assignOrder(command.startDate(), toDo, command.memberId());
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

    private void assignOrder(final LocalDate date, final ToDo toDo, final long memberId) {
        List<OrderWithScheduleOrToDo> toDoList = orderInfoRepository.findOrderInfoWithDetails(date, memberId);
        double insertOrder = getInsertOrder(date, toDoList, toDo);
        OrderInfo createdOrderInfo = createOrderInfo(date, toDo, insertOrder,memberId);
        toDo.updateOrderNum(createdOrderInfo.getOrderNum());
    }

    private double getInsertOrder(final LocalDate date, final List<OrderWithScheduleOrToDo> toDoList, final ToDo toDo) {
        double insertOrder = 1;
        boolean isInserted = false;
        for (OrderWithScheduleOrToDo existingOrder : toDoList) {

            if (!isInserted && existingOrder.getType() == PlanType.TODO) {
                if (toDo.getPriorityValue() > existingOrder.getPriorityValue()) {
                    insertOrder = existingOrder.getOrder();
                    isInserted = true;
                } else if (toDo.getPriorityValue().equals(existingOrder.getPriorityValue())) {
                    if (toDo.getCreatedAt().isBefore(existingOrder.getCreatedAt())) {
                        insertOrder = existingOrder.getOrder();
                        isInserted = true;
                    }
                }
            }

            if (isInserted) {
                existingOrder.shiftBack();
                orderInfoRepository.update(
                        OrderInfo.withId(
                                existingOrder.getOrderInfoId(),
                                existingOrder.getMemberId(),
                                existingOrder.getScheduleId(),
                                existingOrder.getToDoId(),
                                existingOrder.getOrder(),
                                date,
                                existingOrder.getType(),
                                existingOrder.getCreatedAt()
                        ));
            }
        }

        if (!isInserted) {
            insertOrder = toDoList.isEmpty() ? 1 : toDoList.get(toDoList.size() - 1).getOrder() + 1;
        }
        return insertOrder;
    }

    private OrderInfo createOrderInfo(final LocalDate date, final ToDo toDo, final double insertOrder,final long memberId) {
        return orderInfoRepository.save(OrderInfo.of(
                memberId,
                null,
                toDo.getId(),
                insertOrder,
                date,
                PlanType.TODO,
                LocalDateTime.now()
        ));
    }

    private static void checkOwn(final long memberId, final ToDo toDo) {
        if (toDo.getMemberId() != memberId) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

}
