package com.official.memento.global.exception;

public class InvalidAiRequestException extends MementoException {
    public InvalidAiRequestException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
