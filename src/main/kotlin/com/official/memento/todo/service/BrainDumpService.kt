package com.official.memento.todo.service

import com.official.memento.global.entity.enums.RepeatOption
import com.official.memento.orderinfo.domain.OrderInfo
import com.official.memento.orderinfo.domain.OrderInfoRepository
import com.official.memento.orderinfo.domain.OrderWithScheduleOrToDo
import com.official.memento.orderinfo.domain.PlanType
import com.official.memento.tag.domain.TagRepository
import com.official.memento.todo.domain.port.BrainDumpClientOutputPort
import com.official.memento.todo.domain.entity.ToDo
import com.official.memento.todo.domain.repository.ToDoRepository
import com.official.memento.todo.domain.entity.enums.PriorityType
import com.official.memento.todo.domain.entity.enums.ToDoType
import com.official.memento.todo.domain.vo.BrainDump
import com.official.memento.todo.domain.vo.ToDoBrainDump
import com.official.memento.todo.service.command.BrainDumpCreateCommand
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class BrainDumpService(
        private val brainDumpClientOutputPort: BrainDumpClientOutputPort,
        private val toDoRepository: ToDoRepository,
        private val tagRepository: TagRepository,
        private val orderInfoRepository: OrderInfoRepository
) : BrainDumpCreateUseCase {

    companion object {
        private const val DEFAULT_BRAINDUMP_TODO_TAG_ID: Long = 1
    }

    override fun create(command: BrainDumpCreateCommand): ToDoBrainDump {
        val toDoBrainDump =
                brainDumpClientOutputPort.createByBrainDump(
                        BrainDump(
                                command.content,
                        ),
                )
        val tag = tagRepository.findById(DEFAULT_BRAINDUMP_TODO_TAG_ID)
        val toDo =
                ToDo.of(
                        command.memberId,
                        UUID.randomUUID().toString(),
                        toDoBrainDump.createdDate,
                        toDoBrainDump.task,
                        toDoBrainDump.deadline,
                        false,
                        RepeatOption.NONE,
                        null,
                        toDoBrainDump.urgency.toDouble(),
                        toDoBrainDump.importance.toDouble(),
                        toDoBrainDump.urgency * 0.3 + toDoBrainDump.importance * 0.7,
                        PriorityType.findPriorityType(toDoBrainDump.urgency.toDouble(), toDoBrainDump.importance.toDouble()),
                        ToDoType.NORMAL,
                        DEFAULT_BRAINDUMP_TODO_TAG_ID
                )
        val savedToDo = toDoRepository.save(toDo)
        assignOrder(toDoBrainDump.createdDate, savedToDo)
        return toDoBrainDump
    }

    private fun assignOrder(date: LocalDate, toDo: ToDo) {
        val toDoList = orderInfoRepository.findOrderInfoWithDetails(date)
        val insertOrder: Int = getInsertOrder(date, toDoList, toDo)
        val createdOrderInfo = createOrderInfo(date, toDo, insertOrder)
        createdOrderInfo?.updateOrderNum(insertOrder)
        toDo.updateOrderNum(insertOrder)
    }

    private fun getInsertOrder(date: LocalDate, toDoList: List<OrderWithScheduleOrToDo>, toDo: ToDo): Int {
        var insertOrder = 1
        var isInserted = false
        for (existingOrder in toDoList) {
            if (!isInserted && existingOrder.type == PlanType.TODO) {
                if (toDo.priorityValue > existingOrder.priorityValue) {
                    insertOrder = existingOrder.order
                    isInserted = true
                } else if (toDo.priorityValue.equals(existingOrder.priorityValue)) {
                    if (toDo.createdAt.isBefore(existingOrder.createdAt)) {
                        insertOrder = existingOrder.order
                        isInserted = true
                    }
                }
            }

            if (isInserted) {
                existingOrder.shiftBack()
                orderInfoRepository.update(
                        OrderInfo.withId(
                                existingOrder.orderInfoId,
                                existingOrder.scheduleId,
                                existingOrder.toDoId,
                                existingOrder.order,
                                date,
                                existingOrder.type,
                                existingOrder.createdAt
                        )
                )
            }
        }

        if (!isInserted) {
            insertOrder = if (toDoList.isEmpty()) 1 else toDoList[toDoList.size - 1].order + 1
        }
        return insertOrder
    }

    private fun createOrderInfo(date: LocalDate, toDo: ToDo, insertOrder: Int): OrderInfo? {
        return orderInfoRepository.save(
                OrderInfo.of(
                        null,
                        toDo.id,
                        insertOrder,
                        date,
                        PlanType.TODO,
                        LocalDateTime.now()
                )
        )
    }

}

