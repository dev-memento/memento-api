package com.official.memento.todo.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.orderinfo.service.usecase.OrderInfoGetUseCase;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.repository.ToDoRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ToDoQueryService implements ToDoGetUseCase {

    private final ToDoRepository toDoRepository;
    private final OrderInfoGetUseCase orderInfoGetUseCase;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ToDo> getToDos(final long memberId) {
        List<ToDo> todos = toDoRepository.findAllByMemberId(memberId);
        List<ToDo> toDos = todos.stream()
                .peek(todo -> {
                    double order = orderInfoGetUseCase.findByToDoId(todo.getId()).getOrderNum();
                    todo.updateOrderNum(order);
                })
                .toList();
        return sortToDosByStartDateAndOrder(toDos);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ToDo> getTodosByDate(long memberId, LocalDate date) {
        List<ToDo> toDos = toDoRepository.findAllByMemberIdAndStartDate(memberId, date);
        toDos.forEach(todo -> {
            double orderNum = orderInfoGetUseCase.findByToDoId(todo.getId()).getOrderNum();
            todo.updateOrderNum(orderNum);
        });
        return toDos;
    }

    @Transactional(readOnly = true)
    @Override
    public ToDo getDetail(long memberId, long toDoId) {
        ToDo toDo = toDoRepository.findById(toDoId);
        checkOwn(memberId, toDo);
        Tag tag = tagRepository.findById(toDo.getTagId());
        toDo.updateTag(tag);
        return toDo;
    }

    private List<ToDo> sortToDosByStartDateAndOrder(List<ToDo> toDos) {
        return toDos.stream()
                .sorted(Comparator.comparing(ToDo::getStartDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ToDo::getOrderNum, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private static void checkOwn(final long memberId, final ToDo toDo) {
        if (toDo.getMemberId() != memberId) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}
