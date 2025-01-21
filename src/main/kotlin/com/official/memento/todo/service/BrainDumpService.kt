package com.official.memento.todo.service

import com.official.memento.todo.domain.BrainDumpClientOutputPort
import com.official.memento.todo.domain.ToDo
import com.official.memento.todo.domain.ToDoRepository
import com.official.memento.todo.domain.enums.ToDoType
import com.official.memento.todo.domain.vo.BrainDump
import com.official.memento.todo.domain.vo.ToDoBrainDump
import com.official.memento.todo.service.command.BrainDumpCreateCommand
import org.springframework.stereotype.Service

@Service
class BrainDumpService(
    private val brainDumpClientOutputPort: BrainDumpClientOutputPort,
    private val toDoRepository: ToDoRepository,
) : BrainDumpCreateUseCase {
    override fun create(command: BrainDumpCreateCommand): ToDoBrainDump {
        val toDoBrainDump =
            brainDumpClientOutputPort.createByBrainDump(
                BrainDump(
                    command.content,
                ),
            )
        val toDo =
            ToDo.of(
                command.memberId,
                null,
                toDoBrainDump.createdDate,
                toDoBrainDump.task,
                toDoBrainDump.deadline,
                false,
                null,
                null,
                toDoBrainDump.urgency.toDouble(),
                toDoBrainDump.importance.toDouble(),
                toDoBrainDump.urgency * 0.3 + toDoBrainDump.importance * 0.7,
                null,
                ToDoType.NORMAL,
            )
        toDoRepository.save(toDo)
        return toDoBrainDump
    }
}
