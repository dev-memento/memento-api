package com.official.memento.todo.domain.port;

import com.official.memento.todo.domain.vo.BrainDump;
import com.official.memento.todo.domain.vo.ToDoBrainDump;

@FunctionalInterface
public interface BrainDumpClientOutputPort {
    ToDoBrainDump createByBrainDump(BrainDump brainDump);
}
