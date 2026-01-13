package com.official.memento.todo.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidAiRequestException;
import com.official.memento.member.service.result.MemberPersonalInfoResult;
import com.official.memento.member.service.usecase.MemberPersonalInfoGetUseCase;
import com.official.memento.orderinfo.domain.OrderInfo;
import com.official.memento.orderinfo.service.usecase.OrderInfoGetUseCase;
import com.official.memento.orderinfo.service.usecase.OrderInfoUpdateUseCase;
import com.official.memento.tag.service.result.TagResult;
import com.official.memento.tag.service.usecase.TagGetUseCase;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.repository.ToDoRepository;
import com.official.memento.todo.domain.vo.ClaudeAiChatClientOutputPort;
import com.official.memento.todo.service.command.ToDoPrioritizationCommand;
import com.official.memento.todo.service.usecase.ToDoPrioritizationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoPrioritizationService implements ToDoPrioritizationUseCase {

    private final ClaudeAiChatClientOutputPort toDoAiChatClientOutputPort;
    private final ToDoRepository toDoRepository;
    private final OrderInfoGetUseCase orderInfoGetUseCase;
    private final OrderInfoUpdateUseCase orderInfoUpdateUseCase;
    private final TagGetUseCase tagGetUseCase;
    private final MemberPersonalInfoGetUseCase memberPersonalInfoGetUseCase;

    @Override
    @Transactional
    public List<List<ToDo>> prioritizeWeekly(ToDoPrioritizationCommand command) {
        MemberPersonalInfoResult memberPersonalInfo = memberPersonalInfoGetUseCase.findByMemberId(command.memberId());
        List<List<ToDo>> prioritizedToDoList = new ArrayList<>();

        LocalDate currentDate = command.targetDate();

        for (int i = 0; i <= 6; i++) {
            List<ToDo> toDoList = toDoRepository.findAllByMemberIdAndStartDate(
                    command.memberId(),
                    currentDate
            );

            final LocalDate finalCurrentDate = currentDate;
            List<Double> orderList = toDoList.stream()
                    .map(toDo -> orderInfoGetUseCase.findByToDoIdAndDate(toDo.getId(), finalCurrentDate).getOrderNum())
                    .toList();

            List<ToDo> prioritizedToDos = toDoAiChatClientOutputPort.prioritizeTodo(
                    toDoList,
                    orderList,
                    memberPersonalInfo.toPersonalInfoString()
            ).stream()
                    .map(prioritizedToDo -> {
                        OrderInfo orderInfo = orderInfoGetUseCase.findByToDoIdAndDate(
                                prioritizedToDo.id(),
                                finalCurrentDate
                        );
                        orderInfoUpdateUseCase.updateOrderNum(orderInfo, prioritizedToDo.order());

                        ToDo toDo = toDoRepository.findById(prioritizedToDo.id());
                        TagResult tag = tagGetUseCase.findById(toDo.getTagId());

                        return toDo.toBuilder()
                                .priorityUrgency((double) prioritizedToDo.urgency())
                                .priorityImportance((double) prioritizedToDo.importance())
                                .priorityValue((double) prioritizedToDo.priority())
                                .orderNum(prioritizedToDo.order())
                                .tagName(tag.name())
                                .tagColor(tag.color())
                                .build();
                    })
                    .toList();

            prioritizedToDoList.add(i, prioritizedToDos);
            currentDate = currentDate.plusDays(1);
        }

        return prioritizedToDoList;
    }

    @Override
    @Transactional
    public List<ToDo> prioritizeDaily(ToDoPrioritizationCommand command) {
        List<ToDo> toDoList = toDoRepository.findAllByMemberIdAndStartDate(
                command.memberId(),
                command.targetDate()
        );

        if (toDoList.isEmpty()) {
            throw new InvalidAiRequestException(ErrorCode.INVALID_AI_PRIORITIZATION_REQUEST);
        }

        MemberPersonalInfoResult memberPersonalInfo = memberPersonalInfoGetUseCase.findByMemberId(command.memberId());

        List<Double> orderList = toDoList.stream()
                .map(toDo -> orderInfoGetUseCase.findByToDoIdAndDate(toDo.getId(), command.targetDate()).getOrderNum())
                .toList();

        return toDoAiChatClientOutputPort.prioritizeTodo(
                toDoList,
                orderList,
                memberPersonalInfo.toPersonalInfoString()
        ).stream()
                .map(prioritizedToDo -> {
                    OrderInfo orderInfo = orderInfoGetUseCase.findByToDoIdAndDate(
                            prioritizedToDo.id(),
                            command.targetDate()
                    );
                    orderInfoUpdateUseCase.updateOrderNum(orderInfo, prioritizedToDo.order());

                    ToDo toDo = toDoRepository.findById(prioritizedToDo.id());
                    TagResult tag = tagGetUseCase.findById(toDo.getTagId());

                    return toDo.toBuilder()
                            .priorityUrgency((double) prioritizedToDo.urgency())
                            .priorityImportance((double) prioritizedToDo.importance())
                            .priorityValue((double) prioritizedToDo.priority())
                            .orderNum(prioritizedToDo.order())
                            .tagName(tag.name())
                            .tagColor(tag.color())
                            .build();
                })
                .toList();
    }
}
