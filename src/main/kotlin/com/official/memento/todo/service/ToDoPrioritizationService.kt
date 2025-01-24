package com.official.memento.todo.service

import com.official.memento.orderinfo.domain.OrderInfoRepository
import com.official.memento.tag.domain.TagRepository
import com.official.memento.todo.domain.ToDo
import com.official.memento.todo.domain.ToDoRepository
import com.official.memento.todo.domain.ToDoTagRepository
import com.official.memento.todo.domain.vo.ClaudeAiChatClientOutputPort
import com.official.memento.todo.service.command.ToDoPrioritizationCommand
import org.springframework.stereotype.Service

@Service
class ToDoPrioritizationService(
    private val toDoAiChatClientOutputPort: ClaudeAiChatClientOutputPort,
    private val toDoRepository: ToDoRepository,
    private val orderInfoRepository: OrderInfoRepository,
    private val toDoTagRepository: ToDoTagRepository,
    private val tagRepository: TagRepository,
) : ToDoPrioritizationUseCase {
    override fun prioritizeWeekly(command: ToDoPrioritizationCommand) : List<List<ToDo>> {
        val prioritizedToDoList = mutableListOf<List<ToDo>>()
        for (i in 0..6) {
            val toDoList = toDoRepository.findAllByMemberIdAndStartDate(command.memberId, command.targetDate)
            val orderList =
                toDoList.map {
                    orderInfoRepository.findByToDoIdAndDate(it.id, command.targetDate).orderNum
                }.toList()
            prioritizedToDoList.add(
                i,
                toDoAiChatClientOutputPort.prioritizeTodo(toDoList, orderList).map {
                    val orderInfo = orderInfoRepository.findByToDoIdAndDate(it.id, command.targetDate)
                    orderInfoRepository.updateOrderNum(orderInfo, it.order)
                    val toDo = toDoRepository.findById(it.id)
                    val toDoTag = toDoTagRepository.findByToDoId(it.id)
                    val tag = tagRepository.findById(toDoTag.tagId)
                    ToDo.withIdAndTagAndOrder(
                        toDo.id,
                        toDo.memberId,
                        toDo.groupId,
                        toDo.startDate,
                        toDo.description,
                        toDo.endDate,
                        toDo.isCompleted,
                        toDo.repeatOption,
                        toDo.repeatExpiredDate,
                        it.urgency.toDouble(),
                        it.importance.toDouble(),
                        it.priority.toDouble(),
                        toDo.priorityType,
                        toDo.type,
                        toDo.createdAt,
                        toDo.updatedAt,
                        it.order,
                        tag.id,
                        tag.name,
                        tag.color,
                    )
                }.toList(),
            )
            command.targetDate.plusDays(1)
        }
        return prioritizedToDoList
    }

    override fun prioritizeDaily(command: ToDoPrioritizationCommand): List<ToDo> {
        val toDoList = toDoRepository.findAllByMemberIdAndStartDate(command.memberId, command.targetDate)
        val orderList =
            toDoList.map {
                orderInfoRepository.findByToDoIdAndDate(it.id, command.targetDate).orderNum
            }.toList()
        return toDoAiChatClientOutputPort.prioritizeTodo(toDoList, orderList).map {
                val orderInfo = orderInfoRepository.findByToDoIdAndDate(it.id, command.targetDate)
                orderInfoRepository.updateOrderNum(orderInfo, it.order)
                val toDo = toDoRepository.findById(it.id)
                val toDoTag = toDoTagRepository.findByToDoId(it.id)
                val tag = tagRepository.findById(toDoTag.tagId)
                ToDo.withIdAndTagAndOrder(
                    toDo.id,
                    toDo.memberId,
                    toDo.groupId,
                    toDo.startDate,
                    toDo.description,
                    toDo.endDate,
                    toDo.isCompleted,
                    toDo.repeatOption,
                    toDo.repeatExpiredDate,
                    it.urgency.toDouble(),
                    it.importance.toDouble(),
                    it.priority.toDouble(),
                    toDo.priorityType,
                    toDo.type,
                    toDo.createdAt,
                    toDo.updatedAt,
                    it.order,
                    tag.id,
                    tag.name,
                    tag.color,
                )
            }.toList()
    }

}
