package com.official.memento.todo.domain;

import com.official.memento.todo.domain.vo.BrainDump;
import com.official.memento.todo.domain.vo.ToDoBrainDump;

public interface BrainDumpClientOutputPort {
    ToDoBrainDump createByBrainDump(BrainDump brainDump);
}
