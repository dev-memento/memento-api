package com.official.memento.global.lock;

import com.official.memento.global.exception.MementoException;
import com.official.memento.global.exception.ErrorCode;

public class DistributedLockException extends MementoException {

    public DistributedLockException() {
        super(ErrorCode.DISTRIBUTED_LOCK_ACQUISITION_FAILED);
    }
}
