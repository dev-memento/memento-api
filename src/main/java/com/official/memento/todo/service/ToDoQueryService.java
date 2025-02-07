package com.official.memento.todo.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import com.official.memento.orderinfo.domain.OrderInfoRepository;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.todo.domain.ToDo;
import com.official.memento.todo.domain.ToDoRepository;
import com.official.memento.todo.domain.ToDoTag;
import com.official.memento.todo.domain.ToDoTagRepository;
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
    private final OrderInfoRepository orderInfoRepository;
    private final ToDoTagRepository toDoTagRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ToDo> getToDos(final long memberId) {
        List<ToDo> todos = toDoRepository.findAllByMemberId(memberId);
        List<ToDo> toDos = todos.stream()
                .peek(todo -> {
                    Integer order = orderInfoRepository.findOrderByToDoId(todo.getId());
                    ToDoTag toDoTag = toDoTagRepository.findByToDoId(todo.getId());

                    if (toDoTag != null) {
                        Tag tag = tagRepository.findById(toDoTag.getTagId());
                        todo.updateTag(tag);
                    }

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
            int orderNum = orderInfoRepository.findByToDoId(todo.getId()).getOrderNum();
            ToDoTag toDoTag = toDoTagRepository.findByToDoId(todo.getId());
            if (toDoTag != null) {
                Tag tag = tagRepository.findById(toDoTag.getTagId());
                todo.updateTag(tag);
            }
            todo.update(
                    todo.getStartDate(),
                    todo.getDescription(),
                    todo.getEndDate(),
                    todo.getPriorityUrgency(),
                    todo.getPriorityImportance(),
                    orderNum,
                    todo.getPriorityType(),
                    todo.getPriorityValue()
            );
        });
        return toDos;
    }

    @Transactional(readOnly = true)
    @Override
    public ToDo getDetail(long memberId, long toDoId) {
        ToDo toDo = toDoRepository.findById(toDoId);
        checkOwn(memberId, toDo);
        ToDoTag toDoTag = toDoTagRepository.findByToDoId(toDoId);
        if (toDoTag != null) {
            Tag tag = tagRepository.findById(toDoTag.getTagId());
            toDo.updateTag(tag);
            toDo.updateTagId(toDoTag.getTagId());
        }
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
