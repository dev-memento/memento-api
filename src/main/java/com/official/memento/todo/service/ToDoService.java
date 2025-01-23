package com.official.memento.todo.service;

import com.official.memento.global.entity.enums.RepeatOption;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo;
import com.official.memento.orderinfo.domain.PlanType;
import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.todo.controller.dto.ToDoGetResponse;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.domain.ToDoRepository;
import com.official.memento.todo.domain.ToDoTag;
import com.official.memento.todo.domain.ToDoTagRepository;
import com.official.memento.todo.domain.enums.PriorityType;
import com.official.memento.todo.infrastructure.persistence.ToDoTagEntity;
import com.official.memento.todo.service.command.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.official.memento.todo.domain.enums.ToDoType.NORMAL;

@Service
public class ToDoService implements ToDoCreateUseCase, ToDoDeleteUseCase, ToDoUpdateUseCase, ToDoGetUseCase {

    private final ToDoRepository toDoRepository;
    private final ToDoTagRepository toDoTagRepository;
    private final TagRepository tagRepository;
    private final OrderInfoRepository orderInfoRepository;

    public ToDoService(
            final ToDoRepository toDoRepository,
            final ToDoTagRepository toDoTagRepository,
            final TagRepository tagRepository,
            final OrderInfoRepository orderInfoRepository
    ) {
        this.toDoRepository = toDoRepository;
        this.toDoTagRepository = toDoTagRepository;
        this.tagRepository = tagRepository;
        this.orderInfoRepository = orderInfoRepository;
    }

    @Override
    @Transactional
    public void create(final ToDoCreateCommand command) {
        String toDoGroupId = createGroupId();
        if (command.repeatExpiredDate() == null) {
            createSingleToDo(command, toDoGroupId);
        } else {
            createRepeatToDos(command, toDoGroupId);
        }
    }

    @Override
    @Transactional
    public void delete(final ToDoDeleteCommand toDoDeleteCommand) {
        ToDo toDo = toDoRepository.findById(toDoDeleteCommand.toDoId());
        checkOwn(toDoDeleteCommand.memberId(), toDo);
        toDoRepository.deleteById(toDo.getId());
        toDoTagRepository.deleteByToDoId(toDo.getId());
        orderInfoRepository.deleteByToDoId(toDo.getId());
    }

    @Override
    @Transactional
    public void update(final ToDoUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);
        toDo.update(
                command.startDate(),
                command.description(),
                command.endDate(),
                command.priorityUrgency(),
                command.priorityImportance()
        );
        toDoRepository.update(toDo);
        updateOrDeleteTag(toDo, command.tagId());

        if (toDo.getStartDate() != command.startDate() || toDo.getEndDate() != command.endDate()) {
            orderInfoRepository.deleteByToDoId(toDo.getId());
            assignOrder(command.startDate(), toDo);
        }
    }

    @Override
    @Transactional
    public boolean updateCompletion(final ToDoCompletionUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);
        if (toDo.getIsCompleted()) {
            toDo.updateCompletion(false);
        } else {
            toDo.updateCompletion(true);
        }
        return toDoRepository.update(toDo).getIsCompleted();
    }

    @Override
    @Transactional
    public void updatePosition(final ToDoPositionUpdateCommand command) {
        ToDo toDo = toDoRepository.findById(command.toDoId());
        checkOwn(command.memberId(), toDo);

        LocalDate date = orderInfoRepository.findDateByToDoId(command.toDoId());
        int currentOrderNum = orderInfoRepository.findOrderByToDoId(command.toDoId());
        int targetOrderNum = command.targetOrderNum();

        if (currentOrderNum > targetOrderNum) {
            List<OrderInfo> ordersToUpdate = orderInfoRepository.findOrdersBetween(date,targetOrderNum, currentOrderNum - 1);
            for (OrderInfo order : ordersToUpdate) {
                order.incrementOrder();
                orderInfoRepository.update(order);
            }
        } else if (currentOrderNum < targetOrderNum) {
            List<OrderInfo> ordersToUpdate = orderInfoRepository.findOrdersBetween(date,currentOrderNum + 1, targetOrderNum);
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
        ToDo toDo = createToDo(command, toDoGroupId, priorityValue, priorityType, command.startDate());

        if (command.tagId() != null) {
            connectTag(command.tagId(), toDo);
        }
        assignOrder(command.startDate(), toDo);
    }

    private void createRepeatToDos(final ToDoCreateCommand command, final String toDoGroupId) {
        LocalDate currentDate = command.startDate();
        LocalDate repeatExpiredDate = command.repeatExpiredDate();

        while (!currentDate.isAfter(repeatExpiredDate)) {
            Double priorityValue = calculatePriorityValue(command.priorityUrgency(), command.priorityImportance());
            PriorityType priorityType = determinePriorityType(command.priorityUrgency(), command.priorityImportance());

            ToDo toDo = createToDo(command, toDoGroupId, priorityValue, priorityType, currentDate);

            if (command.tagId() != null) {
                connectTag(command.tagId(), toDo);
            }

            if (command.repeatOption() == RepeatOption.DAILY) {
                currentDate = currentDate.plusDays(1);
            } else if (command.repeatOption() == RepeatOption.WEEKLY) {
                currentDate = currentDate.plusWeeks(1);
            } else if (command.repeatOption() == RepeatOption.MONTHLY) {
                currentDate = currentDate.plusMonths(1);
            } else {
                currentDate = currentDate.plusYears(1);
            }

            assignOrder(command.startDate(), toDo);
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
                NORMAL
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

    private void connectTag(final Long tagId, final ToDo toDo) {
        ToDoTag toDoTag = ToDoTag.of(tagId, toDo.getId());
        toDoTagRepository.save(toDoTag);
    }

    private static void checkOwn(final long memberId, final ToDo toDo) {
        if (toDo.getMemberId() != memberId) {
            throw new IllegalArgumentException("해당 투두를 소유하지 않음");//커스텀으로 바꿔야함
        }
    }

    private void updateOrDeleteTag(final ToDo toDo, final Long tagId) {
        ToDoTag toDoTag = toDoTagRepository.findByToDoId(toDo.getId());
        if (tagId == null) {
            toDoTagRepository.deleteByToDoId(toDo.getId());
        } else if (toDoTag == null) {
            toDoTag = ToDoTag.of(tagId, toDo.getId());
            toDoTagRepository.save(toDoTag);
        } else if (toDoTag.getTagId() != tagId) {
            toDoTag.updateTag(tagId, LocalDateTime.now());
            toDoTagRepository.update(toDoTag);
        }
    }

    private void assignOrder(LocalDate date, ToDo toDo) {
        List<OrderWithScheduleOrToDo> toDoList = orderInfoRepository.findOrderInfoWithDetails(date);
        int insertOrder = getInsertOrder(date, toDoList, toDo);
        createOrderInfo(date, toDo, insertOrder);
    }

    private int getInsertOrder(final LocalDate date, final List<OrderWithScheduleOrToDo> toDoList, final ToDo toDo) {
        int insertOrder = 1;
        boolean isInserted = false;
        for (OrderWithScheduleOrToDo existingOrder : toDoList) {

            if (!isInserted && existingOrder.getType() == PlanType.TODO) {
                if (toDo.getPriorityValue() > existingOrder.getPriorityValue()) {
                    insertOrder = existingOrder.getOrder();
                    isInserted = true;
                    System.out.println(1);
                }

                else if (toDo.getPriorityValue().equals(existingOrder.getPriorityValue())) {
                    if (toDo.getCreatedAt().isBefore(existingOrder.getCreatedAt())) {
                        insertOrder = existingOrder.getOrder();
                        isInserted = true;
                    }
                    System.out.println(2);
                }
            }

            if (isInserted) {
                existingOrder.shiftBack();
                orderInfoRepository.update(
                        OrderInfo.withId(
                                existingOrder.getOrderInfoId(),
                                existingOrder.getScheduleId(),
                                existingOrder.getToDoId(),
                                existingOrder.getOrder(),
                                date,
                                existingOrder.getType(),
                                existingOrder.getCreatedAt()
                        ));
                System.out.println(3);
            }
        }

        if (!isInserted) {
            insertOrder = toDoList.isEmpty() ? 1 : toDoList.get(toDoList.size() - 1).getOrder() + 1;
            System.out.println(4);
        }
        return insertOrder;
    }

    private void createOrderInfo(final LocalDate date, final ToDo toDo, final int insertOrder) {
        orderInfoRepository.save(OrderInfo.of(
                null,
                toDo.getId(),
                insertOrder,
                date,
                PlanType.TODO,
                LocalDateTime.now()
        ));
    }

    public List<ToDo> getToDos(final long memberId) {
        List<ToDo> todos = toDoRepository.findAllByMemberId(memberId);
        return todos.stream()
                .peek(todo -> {
                    Integer order = orderInfoRepository.findOrderByToDoId(todo.getId());
                    ToDoTag toDoTag = toDoTagRepository.findByToDoId(todo.getId());

                    if(toDoTag!=null) {
                        Tag tag = tagRepository.findById(toDoTag.getTagId());
                        todo.setTag(tag);
                    }

                    todo.setOrderNum(order);
                })
                .toList();
    }

    @Override
    public List<ToDo> getTodosByDate(long memberId, LocalDate date) {
        List<ToDo> toDos = toDoRepository.findAllByMemberIdAndStartDate(memberId, date);
        toDos.forEach(todo -> {
            int orderNum = orderInfoRepository.findByToDoId(todo.getId()).getOrderNum();
            todo.setOrderNum(orderNum);
        });
        return toDos;
    }
}
