package com.official.memento.todo.domain.vo;

import com.official.memento.todo.domain.entity.ToDo;

import java.util.List;

public interface ClaudeAiChatClientOutputPort {
    List<PrioritizedToDo> prioritizeTodo(
            List<ToDo> todoList,
            List<Double> orderList,
            String personalInfo
    );
}
