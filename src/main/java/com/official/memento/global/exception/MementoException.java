package com.official.memento.global.exception;

public class MementoException extends RuntimeException {

    private final ErrorCode errorCode;

    public MementoException(final ErrorCode errorCode) {
        super("[MementoException] : " + errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
