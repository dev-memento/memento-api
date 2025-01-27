package com.official.memento.todo.service

import com.official.memento.todo.domain.vo.ToDoBrainDump
import com.official.memento.todo.service.command.BrainDumpCreateCommand

@FunctionalInterface
interface BrainDumpCreateUseCase {
    fun create(command: BrainDumpCreateCommand): ToDoBrainDump
}
