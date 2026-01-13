package com.official.memento.todo.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.orderinfo.service.usecase.OrderInfoGetUseCase;
import com.official.memento.tag.service.result.TagResult;
import com.official.memento.tag.service.usecase.TagGetUseCase;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.repository.ToDoRepository;
import com.official.memento.todo.service.result.ToDoResult;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.official.memento.todo.service.usecase.ToDoGetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ToDoQueryService implements ToDoGetUseCase {

    private final ToDoRepository toDoRepository;
    private final OrderInfoGetUseCase orderInfoGetUseCase;
    private final TagGetUseCase tagGetUseCase;

    @Transactional(readOnly = true)
    @Override
    public List<ToDoResult> getToDos(final long memberId) {
        List<ToDo> todos = toDoRepository.findAllByMemberId(memberId);
        List<ToDo> enrichedToDos = todos.stream()
                .map(todo -> {
                    double orderNum = orderInfoGetUseCase.findByToDoId(todo.getId()).getOrderNum();
                    TagResult tagResult = tagGetUseCase.findById(todo.getTagId());
                    return todo.toBuilder()
                            .orderNum(orderNum)
                            .tagName(tagResult.name())
                            .tagColor(tagResult.color())
                            .build();
                })
                .toList();
        return sortToDosByStartDateAndOrder(enrichedToDos).stream()
                .map(ToDoResult::of)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ToDoResult> getTodosByDate(long memberId, LocalDate date) {
        List<ToDo> toDos = toDoRepository.findAllByMemberIdAndStartDate(memberId, date);
        return toDos.stream()
                .map(todo -> {
                    double orderNum = orderInfoGetUseCase.findByToDoId(todo.getId()).getOrderNum();
                    TagResult tagResult = tagGetUseCase.findById(todo.getTagId());
                    return todo.toBuilder()
                            .orderNum(orderNum)
                            .tagName(tagResult.name())
                            .tagColor(tagResult.color())
                            .build();
                })
                .sorted(Comparator.comparing(ToDo::getOrderNum))
                .map(ToDoResult::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ToDoResult getDetail(long memberId, long toDoId) {
        ToDo toDo = toDoRepository.findById(toDoId);
        toDo.checkOwn(memberId);
        TagResult tagResult = tagGetUseCase.findById(toDo.getTagId());
        ToDo enriched = toDo.toBuilder()
                .tagName(tagResult.name())
                .tagColor(tagResult.color())
                .build();
        return ToDoResult.of(enriched);
    }

    private List<ToDo> sortToDosByStartDateAndOrder(List<ToDo> toDos) {
        return toDos.stream()
                .sorted(Comparator.comparing(ToDo::getStartDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ToDo::getOrderNum, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}
