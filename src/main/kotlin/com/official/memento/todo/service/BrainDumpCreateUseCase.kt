package com.official.memento.todo.service

import com.official.memento.todo.domain.vo.ToDoBrainDump
import com.official.memento.todo.service.command.BrainDumpCreateCommand

interface BrainDumpCreateUseCase {
    fun create(command: BrainDumpCreateCommand): ToDoBrainDump
}
